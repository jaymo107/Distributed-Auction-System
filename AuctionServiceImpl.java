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
     * @param item   The item within the auction to bid for.
     * @param seller The seller identifier.
     * @return String     Return the Auction message
     */
    public String createAuction(Item item, User seller) throws RemoteException {
        int key = auctions.size();
        auctions.put(key, new Auction(item, seller, key));
        return "Auction created successfully with ID " + key;
    }

    /**
     * @param auctionId The ID of the auction to bid for.
     * @param amount    The amount to bid by.
     * @param user      The user making the bid.
     * @return int      The success number.
     */
    public String bid(int auctionId, int amount, User user) throws RemoteException {
        if (!auctions.containsKey(auctionId)) {
            return "ERROR: There was no item found with that ID, please try again.";
        }

        return auctions.get(auctionId).bid(amount, user);
    }

    /**
     * Remove the auction from the hashmap and verify the constraints
     * pass such as reserve price etc.
     *
     * @param auctionId The ID of the auction to close.
     * @param seller    The User object of the requesting close.
     * @return String   The message from the server.
     * @throws RemoteException
     */
    public String closeAuction(int auctionId, User seller) throws RemoteException {
        // Check if the auction exists.
        if (!auctions.containsKey(auctionId)) {
            return "ERROR: There was no item found with that ID, please try again.";
        }

        // Store the current auction requested.
        Auction auction = this.auctions.get(auctionId);

        // You must be a buyer to bid on this.
        if (!seller.getEmail().equalsIgnoreCase(auction.getSeller().getEmail())) {
            return "ERROR: You are not the creator of this auction.";
        }

        // The current bid is below the reserve price.
        if (auction.getCurrentBid() < auction.getItem().getReservePrice()) {
            return "ERROR: Reserve price not met.";
        }

        // There has been no bidder yet.
        if (auction.getHighestBidder().getEmail().isEmpty()) {
            return "ERROR: There have been no bids yet.";
        }

        // Store the auction winner and highest bid
        User winner = auction.getHighestBidder();
        int winningBid = auction.getCurrentBid();

        // Remove this auction from the hashmap
        this.auctions.remove(auctionId);

        return "MESSAGE: Item sold! The winner of this auction was " + winner.getName() + " (" + winner.getEmail() + ") with £" + winningBid;
    }

    /**
     * Display a list of currently active auctions.
     *
     * @return String Display the list of auctions.
     * @throws RemoteException
     */
    public String browseAuctions() throws RemoteException {
        if (this.auctions.size() <= 0) {
            return "NOTICE: There are currently no auctions in place.";
        }

        StringBuilder builder = new StringBuilder();

        // Loop over each element in the hashmap and print out the values for each.
        for (int i = 0; i < this.auctions.size(); i++) {
            builder.append("Auction ID: [" + this.auctions.get(i).getId() + "]\n");
            builder.append("Created: " + this.auctions.get(i).getCreatedAt().toString() + "\n");
            builder.append("Description: " + this.auctions.get(i).getItem().getDescription() + "\n");
            builder.append("Current Bid: £" + this.auctions.get(i).getCurrentBid() + "\n");
            builder.append("-------------------------------------------------------------------\n\n");
        }

        return builder.toString();
    }
}
