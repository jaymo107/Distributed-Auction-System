import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

/**
 * Handles the bids and auctions from the various clients
 * and passes messages between the hosts.
 *
 * @author JamesDavies
 */
public class AuctionService extends UnicastRemoteObject implements AuctionServiceSpec {

    private HashMap<Integer, Auction> auctions;

    public AuctionService() throws RemoteException {
        super();
    }

    /**
     * Create a new auction and store it in the 'Auctions' hashmap,
     * generate an index key and return that ID.
     *
     * @param item     The item within the auction to bid for.
     * @param sellerId The seller identifier.
     * @return int     Return the Auction ID
     */
    public int createAuction(Item item, String sellerId) {
        int key = auctions.size();
        auctions.put(key, new Auction(item, sellerId, key));
        return key;
    }

    /**
     * @param auctionId The ID of the auction to bid for.
     * @param amount    The amount to bid by.
     * @return int      The success number.
     */
    public void bid(int auctionId, int amount) {
        if (!auctions.containsKey(auctionId)) {
            System.out.println("There was no item found with that ID, please try again.");
            return;
        }

        auctions.get(auctionId).bid(amount);
        System.out.println("Bid placed for item ID: " + auctionId + " for £" + amount);
    }

    public void browseAuctions() {

    }
}
