import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * Should allow auctions to be created with a given Item
 * and will allow Buyers to bid on that item in the auction.
 *
 * @author JamesDavies
 */
public class SellerClient extends Client {

    private SellerClient() throws RemoteException {
        super();
    }

    public static void main(String[] args) throws RemoteException {
        new SellerClient();
    }
}
