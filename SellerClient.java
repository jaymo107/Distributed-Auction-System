import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Random;
import java.util.Scanner;

/**
 * The client which will create an auction.
 *
 * @author JamesDavies
 */
public class SellerClient implements AuctionClient {

    private AuctionService service;
    private Scanner input;
    private String email;
    private String name;

    public SellerClient() throws RemoteException {
        try {
            this.service = (AuctionService) LocateRegistry.getRegistry(1098)
                    .lookup("rmi://localhost/AuctionService");

            System.out.println("Hello, before we start, please enter the following information...");
            input = new Scanner(System.in);

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

            System.out.println("Success! Connected to Auction server!\n\n1: List the auctions\n2: Create a new auction\n3: Close an auction\n-------------------------------------------");
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

            // List the auctions
            if (currentCommand.equals("1")) {
                System.out.println(this.service.browseAuctions());
            }

            // List the auctions
            if (currentCommand.equals("2")) {
                String description;
                int startingPrice;
                int reservePrice;

                while (true) {
                    System.out.println("Please enter a short description of the item:");
                    description = this.input.nextLine();
                    if (description.length() > 0) break;
                }

                System.out.println("Please enter a start price of this item (£):");
                startingPrice = this.input.nextInt();

                System.out.println("Please enter a reserve price (£):");
                reservePrice = this.input.nextInt();

                Item item = new Item(startingPrice, reservePrice, description);

                System.out.println(this.service.createAuction(item, this.email));
            }

            // Close an auction
            if (currentCommand.equals("3")) {
                System.out.println("Please enter the ID of the auction to close:");
                int auctionId = this.input.nextInt();

                System.out.println(this.service.closeAuction(auctionId, this.email));
            }
        }
    }


    public static void main(String[] args) throws RemoteException {
        new SellerClient();
    }
}
