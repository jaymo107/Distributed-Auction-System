package com.auction;

import org.jgroups.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * The server class to run and serve the remote object to the
 * client.
 *
 * @author JamesDavies
 */
public class Server extends ReceiverAdapter{

    private Channel channel;

    /**
     * Create the AuctionServiceImpl object to serve, and bind the RMI host
     * to the registry to be looked up.
     */
    public Server() {
        try {
            Registry registry = LocateRegistry.getRegistry();

            FrontendImpl service = new FrontendImpl();

            registry.bind("rmi://localhost/BuyerService", service);
            registry.bind("rmi://localhost/SellerService", service);

        } catch (Exception e) {
            System.out.println("Server Error: " + e);
        }
    }


    public static void main(String[] args) {
        new Server();
    }
}
