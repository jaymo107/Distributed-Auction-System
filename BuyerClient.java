import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Random;
import java.util.Scanner;

/**
 * The client which will bid on an auction.
 *
 * @author JamesDavies
 */
public class BuyerClient implements AuctionClient {

    private AuctionService service;
    private Scanner input;

    public BuyerClient() throws RemoteException {
        try {
            this.service = (AuctionService) LocateRegistry.getRegistry(1098)
                    .lookup("rmi://localhost/AuctionService");

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
            if (currentCommand.equalsIgnoreCase("exit")) return;
            this.executeCommand(currentCommand);
        }
    }

    /**
     * Execute the associated command.
     *
     * @param command The command typed in by the user.
     */
    public void executeCommand(String command) throws RemoteException {
        int auctionId;
        int bidAmount;

        // Browse the auctions
        if (command.equalsIgnoreCase("list")) {
            System.out.println(this.service.browseAuctions());
            return;
        }

        // Create a new auction
        if (command.equalsIgnoreCase("bid")) {
            while (true) {
                System.out.println("-------------------------------------------\nPlease enter the ID of the auction you want to bid:");
                auctionId = this.input.nextInt();
                System.out.println("Please enter how much you want to bid (£):");
                bidAmount = this.input.nextInt();
                if (auctionId > 0 && bidAmount > 0) break;
            }

            this.service.bid(auctionId, bidAmount);
            // TODO: Send the client ID with the bid to keep track of who's winning
        }
    }

    public static void main(String[] args) throws RemoteException {
        new BuyerClient();
    }
}
