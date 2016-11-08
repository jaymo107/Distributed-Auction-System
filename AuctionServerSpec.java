import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The remote interface of methods and attributes to be used
 * by the clients, the implementations for these skeletons
 * will be in the 'AuctionServer'
 */
public interface AuctionServerSpec extends Remote {

    /**
     * Create a new auction given an item.
     *
     * @param item
     * @param sellerId
     * @return Return the Auction ID
     */
    public String createAuction(Item item, String sellerId);

    /**
     * Place a bid in an auction.
     *
     * @param auction
     * @param amount
     * @return
     */
    public int bid(Auction auction, int amount);

    /**
     * Print out a list of the current auctions;
     */
    public void browseAuctions();

}
