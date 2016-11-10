import java.rmi.Naming;

/**
 * The client which will bid on an auction.
 *
 * @author JamesDavies
 */
public class BuyerClient {

    public static void main(String[] args) {

        try {
            AuctionServiceSpec service = (AuctionServiceSpec)
                    Naming.lookup("rmi://localhost/AuctionService");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
