import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The remote interface of methods and attributes to be used
 * by the clients, the implementations for these skeletons
 * will be in the 'AuctionServiceImpl'
 *
 * @author JamesDavies
 */
public interface AuctionService extends Remote {

    /**
     * Create a new auction given an item.
     *
     * @param item     The item within the auction to bid for.
     * @param sellerId The seller identifier.
     * @return int     Return the Auction ID
     */
    int createAuction(Item item, String sellerId) throws RemoteException;

    /**
     * Place a bid in an auction.
     *
     * @param auctionId The ID of the auction to bid for.
     * @param amount    The amount to bid by.
     */
    void bid(int auctionId, int amount) throws RemoteException;

    /**
     * Print out a list of the current auctions;
     */
    void browseAuctions() throws RemoteException;

    void sayHello() throws RemoteException;

}
