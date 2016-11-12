import java.io.EOFException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by JamesDavies on 10/11/2016.
 */
public class Client {

    private AuctionService service;
    private Scanner input;
    private String currentCommand;
    private int clientId;

    /**
     * Fetch the remote object, and initialise the input for the
     * commands.
     *
     * @throws RemoteException
     */
    public Client() throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(1098);
            service = (AuctionService) registry.lookup("rmi://localhost/AuctionService");
            input = new Scanner(System.in);
            currentCommand = "";
            clientId = new Random().nextInt(1000);
            System.out.println("Success! Connected to Auction server!\nPlease type a command to get started:\n-------------------------------------------");
            getInput();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Return the auction service remote object from the
     * connection.
     *
     * @return AuctionService
     */
    protected AuctionService getService() {
        return service;
    }

    /**
     * Continuously get input for the commands that they can create.
     *
     * @return String
     */
    protected void getInput() throws RemoteException {
        while (true) {
            currentCommand = input.nextLine();
            if (currentCommand.equalsIgnoreCase("exit")) break;
            executeCommand(currentCommand);
        }
    }

    /**
     * Execute the associated command.
     *
     * @param command The command typed in by the user.
     * @return
     */
    protected void executeCommand(String command) throws RemoteException {

        // Browse the auctions
        if (command.equalsIgnoreCase("list")) {
            System.out.println(service.browseAuctions());
            return;
        }

        if (command.equalsIgnoreCase("create")) {
            String description = "";
            int startPrice = 0;
            int reservePrice = 0;

            while (true) {
                System.out.println("-------------------------------------------\nPlease enter a description for this item:");
                description = this.input.nextLine();
                System.out.println("Please enter a starting price for this item:");
                startPrice = this.input.nextInt();
                System.out.println("Please enter a reserve price:");
                reservePrice = this.input.nextInt();
                if (!description.isEmpty() && startPrice > 0 && reservePrice > 0) break;
            }

            Item item = new Item(startPrice, reservePrice, description);
            int auctionId = service.createAuction(item, this.clientId);
            if (auctionId > 0) {
                System.out.println("SUCCESS: Auction created with ID of #" + auctionId);
                return;
            }
        }

        System.out.println("ERROR: Unknown command, try using the following: list, create, bid, close or exit");
    }
}
