package com.auction;

import org.jgroups.*;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.blocks.RpcDispatcher;
import org.jgroups.util.Util;

import java.io.*;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.rmi.RemoteException;
import java.rmi.server.RemoteObject;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * Handles the bids and auctions from the various clients
 * and passes messages between the hosts.
 *
 * @author JamesDavies
 */
public class AuctionServiceImpl extends ReceiverAdapter {

    private HashMap<Integer, Auction> auctions;
    private Channel channel;
    protected Message sendMessage;
    protected Object receiveMessage;
    private RpcDispatcher dispatcher;
    final List<String> state = new LinkedList<String>();
    private int auctionCounter;

    public AuctionServiceImpl() {

        try {
            // Create the channel
            this.channel = new JChannel();

            // Connect to the cluster
            this.channel.connect("AuctionCluster");

            // Init the dispatcher
            this.dispatcher = new RpcDispatcher(this.channel, this, this, this);

            this.channel.getState(null, 0);

            this.auctionCounter = 0;

            this.auctions = new HashMap<>();

            System.out.println("[SERVER] Server started");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a new auction and store it in the 'Auctions' hashmap,
     * generate an index key and return that ID.
     *
     * @param item   The item within the auction to bid for.
     * @param seller The seller identifier.
     * @return String     Return the Auction message
     */
    public String createAuction(Item item, User seller) throws RemoteException {
        int id = this.auctionCounter++;
        Auction auction = new Auction(item, seller, id);

        int key = auction.getId();
        auctions.put(key, auction);
        return "Auction created successfully with ID " + key;
    }

    /**
     * @param auctionId The ID of the auction to bid for.
     * @param amount    The amount to bid by.
     * @param user      The user making the bid.
     * @return int      The success number.
     */
    public String bid(int auctionId, BigDecimal amount, User user) throws RemoteException {
        if (!auctions.containsKey(auctionId)) {
            return "ERROR: There was no item found with that ID, please try again.";
        }

        return auctions.get(auctionId).bid(amount, user);
    }

    /**
     * Remove the auction from the hash map and verify the constraints
     * pass such as reserve price etc.
     *
     * @param auctionId The ID of the auction to close.
     * @param seller    The User object of the requesting close.
     * @return String   The message from the server.
     * @throws RemoteException
     */
    public String closeAuction(int auctionId, User seller) throws RemoteException {

        // Store the current auction requested.
        Auction auction = this.auctions.get(auctionId);

        if (auction.getSeller().getId() != seller.getId()) {
            return "ERROR: You didn't create this auction, so you can't end it.";
        }

        // Check if the auction exists.
        if (auction == null) {
            return "ERROR: There was no item found with that ID, please try again.";
        }

        // The current bid is below the reserve price.
        if (auction.getCurrentBid().compareTo(auction.getItem().getReservePrice()) == -1) {
            this.auctions.remove(auctionId);
            return "MESSAGE: Closing auction, reserve price not met.";
        }

        // Store the auction winner and highest bid
        User winner = auction.getHighestBidder();
        BigDecimal winningBid = auction.getCurrentBid();

        this.auctions.remove(auctionId);
        return "MESSAGE: Item sold! The winner of this auction was " + winner.getId() + " with £" + winningBid.toString();
    }

    /**
     * Display a list of currently active auctions.
     *
     * @return String Display the list of auctions.
     * @throws RemoteException
     */
    public String browseAuctions() throws RemoteException {
        if (this.auctions.size() <= 0) {
            return "NOTICE: There are currently no auctions in place.";
        }

        StringBuilder builder = new StringBuilder();

        // Loop over each element in the hashmap and print out the values for each.

        Iterator it = this.auctions.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Auction a = this.auctions.get(pair.getKey());

            builder.append("Auction ID: [" + a.getId() + "]\n");
            builder.append("Created: " + a.getCreatedAt().toString() + "\n");
            builder.append("Description: " + a.getItem().getDescription() + "\n");
            builder.append("Current Bid: " + a.getCurrentBid() + " GBP\n");
            builder.append("-------------------------------------------------------------------\n\n");
        }

        return builder.toString();
    }


    public void receive(Message msg) {

    }

    public void viewAccepted(View view) {
        System.out.println(view.getCreator());
    }

    public void getState(OutputStream output) throws Exception {

        Object[] arrayToSend = new Object[]{auctions, this.auctionCounter};

        synchronized (state) {
            Util.objectToStream(arrayToSend, new DataOutputStream(output));
            System.out.println("Get state is being called");
        }
    }

    public void setState(InputStream input) throws Exception {

        Object[] list;

        list = (Object[]) Util.objectFromStream(new DataInputStream(input));

        synchronized (state) {

            HashMap<Integer, Auction> auctionHashmapList = (HashMap<Integer, Auction>) list[0];
            Integer counterToSet = (Integer) list[1];

            auctions = auctionHashmapList;
            this.auctionCounter = counterToSet;

            System.out.println(auctionHashmapList);

            System.out.println("Counter to set: " + counterToSet);
        }
    }
}
