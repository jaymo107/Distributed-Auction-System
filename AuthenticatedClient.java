import java.security.*;

/**
 * Created by JamesDavies on 30/11/2016.
 */
public class AuthenticatedClient {

    protected PublicKey publicKey;
    protected PrivateKey privateKey;

    public void generateKeys() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            KeyPair keys = generator.generateKeyPair();
            this.publicKey = keys.getPublic();
            this.privateKey = keys.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}
