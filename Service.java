import java.rmi.RemoteException;
import java.security.Signature;

/**
 * The remote interface of methods and attributes to be used
 * by the seller client, the implementations for these skeletons
 * will be in the 'AuctionServiceImpl'
 *
 * @author JamesDavies
 */
public interface Service {

    /**
     * Recieve a random value sent by client and return encrypted version with
     * servers private key.
     *
     * @param randomValue
     * @return
     * @throws RemoteException
     */
    Signature verify(String randomValue) throws RemoteException;

    /**
     * Print out a list of the current auctions;
     */
    String browseAuctions() throws RemoteException;

}
