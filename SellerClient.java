import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

/**
 * The client which will create an auction.
 *
 * @author JamesDavies
 */
public class SellerClient implements AuctionClient {

    private SellerService service;
    private Scanner input;
    private User user;

    public SellerClient() throws RemoteException {
        try {
            this.service = (SellerService) LocateRegistry.getRegistry(1098).lookup("rmi://localhost/SellerService");
            System.out.println("[AUCTION SELLER SYSTEM]\n\nHello, before we start, please enter the following information...");
            input = new Scanner(System.in);

            String email;

            // Get the email from the input, keep checking if not valid.
            while (true) {
                System.out.println("Enter email:");
                email = this.input.nextLine();

                if (email.indexOf("@") > 0) break;
            }

            // Create user object for the seller.
            this.user = new User(email, null);

            System.out.println("Success! You are connected to the Auction server!\n\n1: List the auctions\n2: Create a new auction\n3: Close an auction\n-------------------------------------------");
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
                BigDecimal startingPrice;
                BigDecimal reservePrice;

                while (true) {
                    System.out.println("Please enter a short description of the item:");
                    description = this.input.nextLine();
                    if (description.length() > 0) break;
                }

                System.out.println("Please enter a start price of this item (£):");
                startingPrice = this.input.nextBigDecimal();

                System.out.println("Please enter a reserve price (£):");
                reservePrice = this.input.nextBigDecimal();

                Item item = new Item(startingPrice, reservePrice, description);

                System.out.println(this.service.createAuction(item, this.user));
            }

            // Close an auction
            if (currentCommand.equals("3")) {
                System.out.println("Please enter the ID of the auction to close:");
                int auctionId = this.input.nextInt();
                System.out.println(this.service.closeAuction(auctionId, this.user));
            }
        }
    }


    public static void main(String[] args) throws RemoteException {
        new SellerClient();
    }
}
