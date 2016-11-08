import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Should allow auctions to be created with a given Item
 * and will allow Buyers to bid on that item in the auction.
 */
public class SellerClient {

    public static void main(String[] args) throws Exception {

        AuctionServerSpec connection = (AuctionServerSpec) Naming.lookup("rmi://127.0.0.1/auction");

    }
}
