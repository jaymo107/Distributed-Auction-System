import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;

/**
 * The server class to run and serve the remote object to the
 * client.
 * @author JamesDavies
 */
public class Server {

    /**
     * Create the AuctionServiceImpl object to serve, and bind the RMI host
     * to the registry to be looked up.
     */
    public Server() {
        try {
            Registry registry = LocateRegistry.createRegistry(1098);

            AuctionServiceImpl service = new AuctionServiceImpl();

            registry.rebind("rmi://localhost/BuyerService", service);
            registry.rebind("rmi://localhost/SellerService", service);

            System.out.println("Auction server started...");
        }
        catch (Exception e) {
            System.out.println("Server Error: " + e);
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
