/**
 * Should allow auctions to be created with a given Item
 * and will allow Buyers to bid on that item in the auction.
 *
 * @author JamesDavies
 */
public class SellerClient extends Client {

    public SellerClient() {
        AuctionService service = super.connect();
    }

    public static void main(String[] args) {
       new SellerClient();
    }
}
