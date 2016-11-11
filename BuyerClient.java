/**
 * The client which will bid on an auction.
 *
 * @author JamesDavies
 */
public class BuyerClient extends Client{

    public BuyerClient() {
        AuctionService service = super.connect();
    }

    public static void main(String[] args) {
        new BuyerClient();
    }
}
