package com.auction;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

/**
 * The client which will bid on an auction.
 *
 * @author JamesDavies
 */
public class BuyerClient extends AuthenticatedClient implements AuctionClient {

    private BuyerService service;
    private Scanner input;
    private User user;

    /**
     * Locate the remote object in the registry and get the users information.
     */
    public BuyerClient() throws RemoteException {

        try {
//            Registry registry = LocateRegistry.getRegistry(1099);
//            this.service = (BuyerService) registry.lookup("rmi://localhost/BuyerService");
            this.service = (BuyerService) LocateRegistry.getRegistry().lookup("rmi://localhost/BuyerService");
            // Initialise the input stream for the commands
            input = new Scanner(System.in);
            System.out.println("[AUCTION BUYER SYSTEM]\n\nHello, before we start, please enter the following information...");

            int id;

            // Check the email is valid.
            while (true) {
                System.out.println("Enter user ID:");
                id = this.input.nextInt();

                if (id > 0) break;
            }

            // Create a new user object to pass to the server.
            this.user = new User(id);

            if (!this.authenticate(this.user, this.service)) {
                // Authentication failed
                System.out.println("Sorry, we couldn't seem to authenticate you.");
                System.exit(0);
            }

            System.out.println("Thanks, You have connected to the auction server!\nPlease type a command to get started:\n1: Browse Auctions\n2: Bid on an Auction\n-------------------------------------------");
            this.getInput();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Continuously get input for the commands that they can create, match
     * them and execute the appropriate method.
     */
    public void getInput() throws Exception {
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
                int auctionId;
                BigDecimal amount;

                System.out.println("Please enter the ID of the auction you want to bid:");
                auctionId = this.input.nextInt();

                while (true) {
                    System.out.println("Please enter the amount you want to bid (£):");
                    amount = this.input.nextBigDecimal();
                    if (amount.compareTo(new BigDecimal(0)) > 0) break;
                }

                System.out.println(this.service.bid(auctionId, amount, this.user));
            }
        }
    }

    public static void main(String[] args) throws RemoteException {
        new BuyerClient();
    }
}
