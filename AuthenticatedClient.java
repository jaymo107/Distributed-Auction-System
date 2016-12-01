import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.rmi.Remote;
import java.security.*;

/**
 * Created by JamesDavies on 30/11/2016.
 */
public class AuthenticatedClient {

    protected PublicKey publicKey;
    protected PrivateKey privateKey;

    /**
     * Generate a key pair if they don't exist already.
     */
    public void generateKeys() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            KeyPair keys = generator.generateKeyPair();

            // Only generate keys if they don't already exist
            if ((this.publicKey != null) || (this.privateKey != null)) return;

            this.publicKey = keys.getPublic();
            this.privateKey = keys.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * Encrypt using our public key.
     *
     * @param string
     * @param publicKey
     * @return
     */
    public byte[] encrypt(String string, PublicKey publicKey) {
        byte[] encrypted = null;

        try {
            // Use RSA encryption
            Cipher cipher = Cipher.getInstance("RSA");

            // Set the cipher to encrypt mode, and use our public key
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encrypted = cipher.doFinal(string.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encrypted;
    }

    /**
     * Decrypt using our private key.
     *
     * @param encrypted
     * @param privateKey
     * @return
     */
    public String decrypt(byte[] encrypted, PrivateKey privateKey) {
        byte[] decrypted = null;

        try {
            // Use RSA encryption
            Cipher cipher = Cipher.getInstance("RSA");

            // Set the cipher to decrypt mode, and use our private key
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            decrypted = cipher.doFinal(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return decrypted.toString();
    }

    /**
     * Authenticate the user with the server.
     *
     * @param $user
     * @return
     */
    public boolean authenticate(User $user, Service service) {


        return false;
    }

}
