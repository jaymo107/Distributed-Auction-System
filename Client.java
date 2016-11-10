import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by JamesDavies on 10/11/2016.
 */
public class Client {

    public AuctionService connect() {
        AuctionService connection = null;
        try {
            Registry registry = LocateRegistry.getRegistry(1098);
            connection = (AuctionService)
                    registry.lookup("rmi://localhost/AuctionService");
            System.out.println("Success! Connected to Auction server!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

}
