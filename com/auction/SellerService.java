package com.auction;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The remote interface of methods and attributes to be used
 * by the seller client, the implementations for these skeletons
 * will be in the 'AuctionServiceImpl'
 *
 * @author JamesDavies
 */
public interface SellerService extends Service {

    /**
     * Create a new auction given an item.
     *
     * @param item    The item within the auction to bid for.
     * @param user    The seller identifier.
     * @return String Return the Auction ID
     */
    String createAuction(Item item, User user) throws Exception;

    /**
     * Place a bid in an auction.
     *
     * @param auctionId The ID of the auction to close.
     * @param user      The email of the client closing the auction.
     */
    String closeAuction(int auctionId, User user) throws Exception;


}
