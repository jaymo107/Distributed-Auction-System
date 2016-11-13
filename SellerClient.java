import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;
import java.util.Scanner;

/**
 * The client which will create an auction.
 *
 * @author JamesDavies
 */
public class SellerClient implements AuctionClient {

    private AuctionService service;
    private int clientId;
    private Scanner input;

    public SellerClient() throws RemoteException {
        try {
            this.service = (AuctionService) LocateRegistry.getRegistry(1098)
                    .lookup("rmi://localhost/AuctionService");
            this.clientId = new Random().nextInt(1000);
            input = new Scanner(System.in);
            System.out.println("Success! Connected to Auction server!\nPlease type a command to get started:\n-------------------------------------------");
            this.getInput();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Continuously get input for the commands that they can create.
     */
    public void getInput() throws RemoteException {
        String currentCommand;
        while (true) {
            currentCommand = this.input.nextLine();
            if (currentCommand.equalsIgnoreCase("exit")) break;
            System.out.println(currentCommand);
            this.executeCommand(currentCommand);
        }
    }

    /**
     * Execute the associated command.
     *
     * @param command The command typed in by the user.
     */
    public void executeCommand(String command) throws RemoteException {
        String description;
        int startPrice;
        int reservePrice;

        // Browse the auctions
        if (command.equalsIgnoreCase("list")) {
            System.out.println(this.service.browseAuctions());
            return;
        }

        // Create a new auction
        if (command.equalsIgnoreCase("create")) {
            System.out.println("MAKE NEW");
            while (true) {
                System.out.println("-------------------------------------------\nPlease enter a description for this item:");
                description = this.input.nextLine();
                System.out.println("Please enter a starting price for this item:");
                startPrice = this.input.nextInt();
                System.out.println("Please enter a reserve price:");
                reservePrice = this.input.nextInt();
                if (!description.isEmpty() && startPrice > 0 && reservePrice > 0) break;
            }

            int auctionId = service.createAuction(new Item(startPrice, reservePrice, description), this.clientId);

            if (auctionId > 0) {
                System.out.println("SUCCESS: Auction created with ID of #" + auctionId);
            }
        }
    }

    public static void main(String[] args) throws RemoteException {
        new BuyerClient();
    }
}
