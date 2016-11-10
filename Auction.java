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
    private String sellerId;
    private Date createdAt;
    private int currentBid;
    private boolean isActive;

    /**
     * Create an Auction with the item to sell and the seller.
     *
     * @param item     The item within the auction.
     * @param sellerId The identifier of the seller.
     * @param id       The generated ID for this current auction.
     */
    public Auction(Item item, String sellerId, int id) {
        this.item = item;
        this.sellerId = sellerId;
        this.createdAt = new Date();
        this.id = id;
        this.currentBid = 0;
        this.isActive = true;
    }

    /**
     * Increase the current bid, returns true if the bid has
     * successfully been made.
     *
     * @param amount The Amount to bid on the item for.
     */
    public synchronized void bid(int amount) {
        if (isActive()) {
            this.currentBid += amount;
        }
    }

    /**
     * End the current auction.
     */
    public void closeAuction() {
        this.isActive = false;
    }

    /**
     * @return String
     */
    public String getSellerId() {
        return sellerId;
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
     * @return boolean
     */
    public boolean isActive() {
        return isActive;
    }
}
