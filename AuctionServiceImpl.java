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
    private String highestBidderEmail;

    public AuctionServiceImpl() throws RemoteException {
        super();
        this.auctions = new HashMap<>();
    }

    /**
     * Create a new auction and store it in the 'Auctions' hashmap,
     * generate an index key and return that ID.
     *
     * @param item        The item within the auction to bid for.
     * @param clientEmail The seller identifier.
     * @return String     Return the Auction message
     */
    public String createAuction(Item item, String clientEmail) throws RemoteException {
        int key = auctions.size();
        auctions.put(key, new Auction(item, clientEmail, key));
        return "Auction created successfully with ID " + key;
    }

    /**
     * @param auctionId The ID of the auction to bid for.
     * @param amount    The amount to bid by.
     * @return int      The success number.
     */
    public String bid(int auctionId, int amount, String bidderEmail) throws RemoteException {
        if (!auctions.containsKey(auctionId)) {
            return "ERROR: There was no item found with that ID, please try again.";
        }

        auctions.get(auctionId).bid(amount, bidderEmail);
        return "LOG: Bid placed for item ID: " + auctionId + " for £" + amount;
    }

    public String closeAuction(int auctionId, String clientEmail) throws RemoteException {
        if (!auctions.containsKey(auctionId)) {
            return "ERROR: There was no item found with that ID, please try again.";
        }

        Auction auction = this.auctions.get(auctionId);
        auction.closeAuction();

        /**
         * TODO: Find out the winning person here.
         */
        return "Auction closed, the winner of this auction was {NAME} with £" + auction.getCurrentBid();
    }

    public String browseAuctions() throws RemoteException {
        if (this.getAuctions().size() <= 0) {
            return "NOTICE: There are currently no auctions in place.";
        }

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < this.auctions.size(); i++) {
            builder.append("Auction ID: [" + this.auctions.get(i).getId() + "]\n");
            builder.append("Created: " + this.auctions.get(i).getCreatedAt().toString() + "\n");
            builder.append("Description: " + this.auctions.get(i).getItem().getDescription() + "\n");
            builder.append("Current Bid: " + this.auctions.get(i).getCurrentBid() + "\n");
            builder.append("-------------------------------------------------------------------\n\n");
        }

        return builder.toString();
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
