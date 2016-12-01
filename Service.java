import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The remote interface of methods and attributes to be used
 * by the seller client, the implementations for these skeletons
 * will be in the 'AuctionServiceImpl'
 *
 * @author JamesDavies
 */
public interface Service {




    /**
     * Print out a list of the current auctions;
     */
    String browseAuctions() throws RemoteException;

}
