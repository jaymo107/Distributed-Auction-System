import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

/**
 * Handles the bids and auctions from the various clients
 * and passes messages between the hosts.
 */
public class AuctionServer extends UnicastRemoteObject implements AuctionServerSpec {

    /** This variable keeps track on all of the currently running auctions. */
    private HashMap<String, Auction> auctions;

    protected AuctionServer() throws RemoteException {

    }

    public static void main(String[] args) {
        
    }

    public String seyHello() throws RemoteException {
        return "Hello World";
    }
}
