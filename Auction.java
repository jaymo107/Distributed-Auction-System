import java.util.Date;

/**
 * A class containing details of an existing Auction, stores
 * the Item being sold and the auction ID.
 *
 * @author JamesDavies
 */
public class Auction {
    private Item item;
    private int id;
    private String sellerEmail;
    private String highestBidderEmail;
    private Date createdAt;
    private int currentBid;

    /**
     * Create an Auction with the item to sell and the seller.
     *
     * @param item        The item within the auction.
     * @param sellerEmail The identifier of the seller.
     * @param id          The generated ID for this current auction.
     */
    public Auction(Item item, String sellerEmail, int id) {
        this.item = item;
        this.sellerEmail = sellerEmail;
        this.createdAt = new Date();
        this.id = id;
        this.currentBid = item.getStartingPrice();
        this.highestBidderEmail = "";
    }

    /**
     * Increase the current bid, returns true if the bid has
     * successfully been made.
     *
     * @param amount The Amount to bid on the item for.
     */
    public synchronized String bid(int amount, String bidEmail) {

        // Trying to bid on own item
        if (bidEmail.equalsIgnoreCase(this.sellerEmail)) {
            return "ERROR: You are trying to bid on your own auction.";
        }

        // Check that the bid is bigger than the current
        if (amount <= this.currentBid) {
            return "ERROR: Bid must be higher than current bid.";
        }

        this.currentBid = amount;
        this.highestBidderEmail = bidEmail;
        return "SUCCESS: Bid placed successfully for £" + this.currentBid;
    }

    /**
     * @return int
     */
    public int getId() {
        return id;
    }

    /**
     * @return Item
     */
    public Item getItem() {
        return item;
    }

    /**
     * @return Date
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * @return int
     */
    public int getCurrentBid() {
        return currentBid;
    }

    /**
     * @return String
     */
    public String getHighestBidderEmail() {
        return highestBidderEmail;
    }

    /**
     * @return String
     */
    public String getSellerEmail() {
        return sellerEmail;
    }
}
