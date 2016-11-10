import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The remote interface of methods and attributes to be used
 * by the clients, the implementations for these skeletons
 * will be in the 'AuctionService'
 *
 * @author JamesDavies
 */
public interface AuctionServiceSpec extends Remote {

    /**
     * Create a new auction given an item.
     *
     * @param item     The item within the auction to bid for.
     * @param sellerId The seller identifier.
     * @return int     Return the Auction ID
     */
    public int createAuction(Item item, String sellerId);

    /**
     * Place a bid in an auction.
     *
     * @param auctionId The ID of the auction to bid for.
     * @param amount    The amount to bid by.
     */
    public void bid(int auctionId, int amount);

    /**
     * Print out a list of the current auctions;
     */
    public void browseAuctions();

}
