import java.rmi.RemoteException;

/**
 * The client which will bid on an auction.
 *
 * @author JamesDavies
 */
public class BuyerClient extends Client{

    public BuyerClient() throws RemoteException {
        super();
    }

    public static void main(String[] args) throws RemoteException {
        new BuyerClient();
    }
}
