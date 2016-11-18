import java.io.Serializable;
import java.math.BigDecimal;

/**
 * An object which can be sold within the auction class. Holds
 * the information about the item such as pricing.
 *
 * @author JamesDavies
 */
public class Item implements Serializable {
    private BigDecimal startingPrice;
    private String description;
    private BigDecimal reservePrice;

    public Item(BigDecimal startingPrice, BigDecimal reservePrice, String description) {
        this.startingPrice = startingPrice;
        this.reservePrice = reservePrice;
        this.description = description;
    }

    public BigDecimal getStartingPrice() {
        return startingPrice;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getReservePrice() {
        return reservePrice;
    }
}
