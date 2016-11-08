import java.util.Date;

/**
 * A class containing details of an existing Auction, stores
 * the Item being sold and the auction ID.
 */
public class Auction {
    private Item item;
    private String id;
    private String sellerId;

    public Auction(Item item, String sellerId) {
        this.item = item;
        this.sellerId = sellerId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getId() {
        return id;
    }

    public Item getItem() {
        return item;
    }
}
