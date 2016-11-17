import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

/**
 * The client which will bid on an auction.
 *
 * @author JamesDavies
 */
public class BuyerClient implements AuctionClient {

    private AuctionService service;
    private Scanner input;
    private String email;
    private String name;

    public BuyerClient() throws RemoteException {
        try {
            this.service = (AuctionService) LocateRegistry.getRegistry(1098)
                    .lookup("rmi://localhost/AuctionService");

            input = new Scanner(System.in);

            System.out.println("Hello, before we start, please enter the following information...");

            while (true) {
                System.out.println("Enter email:");
                this.email = this.input.nextLine();
                if (this.email.indexOf("@") > 0) break;
            }

            while (true) {
                System.out.println("Enter name:");
                this.name = this.input.nextLine();
                if (this.name.length() > 0) break;
            }

            System.out.println("Thanks " + this.name + ", Connected to Auction server!\nPlease type a command to get started:\n1: Browse Auctions\n2: Bid on an Auction\n-------------------------------------------");
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

            // List the auctions
            if (currentCommand.equals("1")) {
                System.out.println(this.service.browseAuctions());
            }

            // Bid on an item
            if (currentCommand.equals("2")) {
                int auctionId = 0;
                int amount = 0;

                System.out.println("Please enter the ID of the auction you want to bid:");
                auctionId = this.input.nextInt();

                while (true) {
                    System.out.println("Please enter the amount you want to bid (£):");
                    amount = this.input.nextInt();
                    if (amount > 0) break;
                }

                System.out.println(this.service.bid(auctionId, amount, this.email));
            }
        }
    }

    public static void main(String[] args) throws RemoteException {
        new BuyerClient();
    }
}
