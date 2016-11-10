import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * The server class to run and serve the remote object to the
 * client.
 * @author JamesDavies
 */
public class AuctionServer {

    /**
     * Create the AuctionService object to serve, and bind the RMI host
     * to the registry to be looked up.
     */
    public AuctionServer() {
        try {
            Registry registry = LocateRegistry.createRegistry(1098);
            
            registry.rebind("rmi://localhost/AuctionService", new AuctionService());
            System.out.println("Server started...");
        }
        catch (Exception e) {
            System.out.println("Server Error: " + e);
        }
    }

    public static void main(String[] args) {
        new AuctionServer();
    }
}
