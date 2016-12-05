import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.security.Signature;
import java.security.SignedObject;

/**
 * The remote interface of methods and attributes to be used
 * by the seller client, the implementations for these skeletons
 * will be in the 'AuctionServiceImpl'
 *
 * @author JamesDavies
 */
public interface Service {

    /**
     * Verify the server by recieving a random value sent by client and return encrypted version with
     * servers private key.
     *
     * @param authObject
     * @return SignedObject
     * @throws RemoteException
     */
    SignedObject verify(Auth authObject) throws RemoteException, UnknownHostException;

    /**
     * Verify the clients authenticity by decrypting the clients object
     * using it's public key.
     *
     * @param signedObject
     * @return boolean
     * @throws RemoteException
     */
    boolean verifyClient(SignedObject signedObject) throws RemoteException;

    /**
     * Print out a list of the current auctions;
     */
    String browseAuctions() throws RemoteException;

}
