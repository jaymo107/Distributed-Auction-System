import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * The remote interface of methods and attributes to be used
 * by the clients, the implementations for these skeletons
 * will be in the 'AuctionServiceImpl'
 *
 * @author JamesDavies
 */
public interface AuctionService extends Remote {

    HashMap<Integer, Auction> auctions = new HashMap<>();

    /**
     * Create a new auction given an item.
     *
     * @param item     The item within the auction to bid for.
     * @param user     The seller identifier.
     * @return int     Return the Auction ID
     */
    String createAuction(Item item, User user) throws RemoteException;

    /**
     * Place a bid in an auction.
     *
     * @param auctionId The ID of the auction to bid for.
     * @param amount    The amount to bid by.
     * @param user      The user making the bid.
     */
    String bid(int auctionId, int amount, User user) throws RemoteException;

    /**
     * Place a bid in an auction.
     *
     * @param auctionId    The ID of the auction to close.
     * @param user         The email of the client closing the auction.
     */
    String closeAuction(int auctionId, User user) throws RemoteException;

    /**
     * Print out a list of the current auctions;
     */
    String browseAuctions() throws RemoteException;

}
