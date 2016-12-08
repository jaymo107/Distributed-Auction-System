package com.auction;

import java.math.BigDecimal;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The remote interface of methods and attributes to be used
 * by the buyer client, the implementations for these skeletons
 * will be in the 'AuctionServiceImpl'
 *
 * @author JamesDavies
 */
public interface BuyerService extends Service {

    /**
     * Place a bid in an auction.
     *
     * @param auctionId The ID of the auction to bid for.
     * @param amount    The amount to bid by.
     * @param user      The user making the bid.
     */
    String bid(int auctionId, BigDecimal amount, User user) throws Exception;


}
