/**
 * An object which can be sold within the auction class. Holds
 * the information about the item such as pricing.
 */
public class Item {
    private int startingPrice;
    private String description;
    private int reservePrice;

    public Item(int startingPrice, int reservePrice, String description) {
        this.startingPrice = startingPrice;
        this.reservePrice = reservePrice;
        this.description = description;
    }

    public int getStartingPrice() {
        return startingPrice;
    }

    public String getDescription() {
        return description;
    }

    public int getReservePrice() {
        return reservePrice;
    }
}
