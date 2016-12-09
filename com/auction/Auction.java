package com.auction;

import java.math.BigDecimal;
import java.util.Date;

/**
 * A class containing details of an existing Auction, stores
 * the Item being sold and the auction ID.
 *
 * @author JamesDavies
 */
public class Auction {
    private Item item;
    private int id;
    private User seller;
    private User highestBidder;
    private Date createdAt;
    private BigDecimal currentBid;
    public int auctionIncrementId = 0;

    /**
     * Create an Auction with the item to sell and the seller.
     *
     * @param item   The item within the auction.
     * @param seller The identifier of the seller.
     */
    public Auction(Item item, User seller, int id) {
        this.item = item;
        this.seller = seller;
        this.createdAt = new Date();
        this.id = id;
        this.currentBid = item.getStartingPrice();
        this.highestBidder = new User(0);
    }

    /**
     * Increase the current bid, returns true if the bid has
     * successfully been made.
     *
     * @param amount The Amount to bid on the item for.
     * @param user   The user bidding on this item.
     */
    public synchronized String bid(BigDecimal amount, User user) {

        // Trying to bid on own item
        if (user.getId() == this.seller.getId()) {
            return "ERROR: You are trying to bid on your own auction.";
        }

        // Check that the bid is bigger than the current
        if (amount.compareTo(this.currentBid) < 1) {
            return "ERROR: Bid must be higher than current bid.";
        }

        this.currentBid = amount;
        this.highestBidder = user;
        return "SUCCESS: Bid placed successfully for £" + this.currentBid;
    }

    /**
     * @return int
     */
    public int getId() {
        return id;
    }

    /**
     * @return Item
     */
    public Item getItem() {
        return item;
    }

    /**
     * @return Date
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * @return int
     */
    public BigDecimal getCurrentBid() {
        return currentBid;
    }

    /**
     * @return User
     */
    public User getHighestBidder() {
        return highestBidder;
    }

    /**
     * @return User
     */
    public User getSeller() {
        return this.seller;
    }
}
