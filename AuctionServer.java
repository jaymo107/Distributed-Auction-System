import java.rmi.Naming;

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
            AuctionService service = new AuctionService();
            Naming.rebind("rmi://localhost/AuctionService", service);
        }
        catch (Exception e) {
            System.out.println("Server Error: " + e);
        }
    }

    public static void main(String[] args) {
        new AuctionServer();
    }
}
