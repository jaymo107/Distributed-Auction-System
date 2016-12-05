import java.io.*;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.security.*;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.*;
import java.util.Random;
import java.util.Random.*;


public class AuthenticatedClient {

    /**
     * AUTHENTICATION
     * [Object with info to send]
     * 0. Send id and random number to server - plain text
     * 1. Server encrypts with its private key and sends it back with some data  i.e. IP address and signs it
     * 2. Client receives and decrypts. Compares number. Returns OK.
     * 3. Upon OK, server sends Client a random number
     * 4. Client encrypts and signs data and sends back with extra data i.e. IP address
     * 5. Server then looks up userID(?) gets key and decrypts. Compares and verifies.
     */

    private PublicKey publicKey;
    private PrivateKey privateKey;
    private PublicKey serverPublic;
    private final String keyDir = "./keys/";
    private final String publicKeyDir = keyDir + "public/";
    private final String privateKeyDir = keyDir + "private/";
    private Signature signature;

    /**
     * Load the keys from the client and server.
     *
     * @param userId
     */
    private void loadKeys(int userId) {

        String filename = userId + ".key";
        String privateKeyLocation = this.privateKeyDir + filename;
        String publicKeyLocation = this.publicKeyDir + filename;

        // Check the file exists
        if (!new File(privateKeyLocation).exists()) {
            return;
        }

        if (!new File(publicKeyLocation).exists()) {
            return;
        }

        try {
            // Read in private key
            FileInputStream fs = new FileInputStream(new File(privateKeyLocation));
            ObjectInputStream ds = new ObjectInputStream(fs);
            byte[] prKey = (byte[]) ds.readObject();
            fs.close();
            ds.close();
            KeyFactory keyFactory = KeyFactory.getInstance("DSA");
            KeySpec ks = new PKCS8EncodedKeySpec(prKey);
            this.privateKey = (DSAPrivateKey) keyFactory.generatePrivate(ks);

            // Load in public key
            fs = new FileInputStream(new File(publicKeyLocation));
            ds = new ObjectInputStream(fs);
            byte[] puKey = (byte[]) ds.readObject();
            fs.close();
            ds.close();
            ks = new X509EncodedKeySpec(puKey);
            this.publicKey = (DSAPublicKey) keyFactory.generatePublic(ks);

            // Load in server public key
            fs = new FileInputStream(new File(publicKeyDir + "server.key"));
            ds = new ObjectInputStream(fs);
            byte[] serverPublic = (byte[]) ds.readObject();
            fs.close();
            ds.close();
            ks = new X509EncodedKeySpec(serverPublic);
            this.serverPublic = (DSAPublicKey) keyFactory.generatePublic(ks);

            FileInputStream input = new FileInputStream(new File("./keys/public/server.key"));
            ObjectInputStream ois = new ObjectInputStream(input);
            byte[] svrPub = (byte[]) ois.readObject();
            input.close();
            ois.close();
            KeyFactory keys = KeyFactory.getInstance("DSA");
            KeySpec spec = new X509EncodedKeySpec(svrPub);

            this.serverPublic = (DSAPublicKey) keyFactory.generatePublic(ks);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate the Key pair to be used with authentication.
     *
     * @param user
     */
    public void generateKeys(int user) {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("DSA");
            generator.initialize(1024, new SecureRandom());
            KeyPair keys = generator.generateKeyPair();

            // Only generate keys if they don't already exist
            if ((this.publicKey != null) || (this.privateKey != null)) return;

            this.publicKey = keys.getPublic();
            this.privateKey = keys.getPrivate();

            ObjectOutputStream fos = new ObjectOutputStream(new FileOutputStream("./keys/public/" + user + ".key"));
            fos.writeObject(this.publicKey.getEncoded());
            fos.close();

            System.out.println("Public key written.");

            fos = new ObjectOutputStream(new FileOutputStream("./keys/private/" + user + ".key"));
            fos.writeObject(this.privateKey.getEncoded());
            fos.close();

            System.out.println("Private key written.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sign the object using my private key.
     *
     * @param authObject
     * @return
     * @throws Exception
     */
    private SignedObject sign(Auth authObject) throws Exception {
        SignedObject obj = null;

        try {
            authObject.setOriginIp(Inet4Address.getLocalHost().toString());
            authObject.setValue(String.valueOf(new Random().nextInt(100)));
            obj = new SignedObject(authObject, this.privateKey, this.signature);

            return obj;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    /**
     * Authenticate the user with the server.
     *
     * @param $user
     * @return
     */
    public synchronized boolean authenticate(User user, Service service) throws Exception {

        this.signature = Signature.getInstance("DSA");

        // Ensure the keys are loaded
        loadKeys(user.getId());

        // Ensure all of the keys exist.
        if (this.privateKey == null || this.serverPublic == null || this.publicKey == null) {
            return false;
        }

        // Step 1: Send a plain user Auth object to the server.
        Auth authObject = new Auth(user.getId());

        // Step 2: Recieve the sealed and signed object from the server
        SignedObject obj = service.verify(authObject);
        authObject = (Auth) obj.getObject();

        // If couldn't verify the server or the values don't match then return.
        if (!obj.verify(this.serverPublic, signature) ||
                !authObject.getValue().equals(authObject.getValue())) {
            return false;
        }

        System.out.println("[AUTH] Server verified!");

        // Step 3: Get the signed object to send to the server.
        SignedObject signedObject = this.sign(authObject);

        return !!(service.verifyClient(signedObject));
    }
}
