import sun.tools.java.Environment;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

/**
 * Handles the bids and auctions from the various clients
 * and passes messages between the hosts.
 *
 * @author JamesDavies
 */
public class AuctionServiceImpl extends UnicastRemoteObject implements AuctionService {

    private HashMap<Integer, Auction> auctions;

    public AuctionServiceImpl() throws RemoteException {
        super();
        this.auctions = new HashMap<>();
    }

    /**
     * Create a new auction and store it in the 'Auctions' hashmap,
     * generate an index key and return that ID.
     *
     * @param item     The item within the auction to bid for.
     * @param sellerId The seller identifier.
     * @return int     Return the Auction ID
     */
    public int createAuction(Item item, int sellerId) throws RemoteException {
        int key = auctions.size();
        auctions.put(key, new Auction(item, sellerId, key));
        return key;
    }

    /**
     * @param auctionId The ID of the auction to bid for.
     * @param amount    The amount to bid by.
     * @return int      The success number.
     */
    public void bid(int auctionId, int amount) throws RemoteException {
        if (!auctions.containsKey(auctionId)) {
            System.out.println("ERROR: There was no item found with that ID, please try again.");
            return;
        }

        auctions.get(auctionId).bid(amount);
        System.out.println("LOG: Bid placed for item ID: " + auctionId + " for £" + amount);
    }

    public String browseAuctions() throws RemoteException {
        if(this.getAuctions().size() <= 0) {
            return "NOTICE: There are currently no auctions in place. Create one!";
        }

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < this.auctions.size(); i++) {
            builder.append(this.auctions.get(i).getCurrentBid());
            builder.append("\t");
            builder.append(this.auctions.get(i).getItem().getDescription());
            builder.append("\n");
        }

        return String.valueOf(this.auctions.size());
    }

    /**
     * Get the auctions hashmap.
     *
     * @return
     */
    public HashMap<Integer, Auction> getAuctions() throws RemoteException {
        return this.auctions;
    }
}
