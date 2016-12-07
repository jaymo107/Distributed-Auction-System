package com.auction;

import org.jgroups.*;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.blocks.RpcDispatcher;

import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.security.SignedObject;
import java.util.ArrayList;

/**
 * @author JamesDavies
 * @date 07/12/2016
 * Auctioning System
 */
public class FrontendImpl extends ReceiverAdapter implements BuyerService, SellerService {

    private ArrayList<ReplicaManager> replicaManagers;
    private Channel channel;
    private RpcDispatcher dispatcher;
    private RequestOptions reqOpts;

    /**
     * Met
     *
     * @throws Exception
     */
    public FrontendImpl() throws Exception {
        this.replicaManagers = new ArrayList<>();
        this.channel = new JChannel();
        this.channel.setReceiver(this);
        this.reqOpts = new RequestOptions(ResponseMode.GET_ALL, 5000);
        this.channel.connect("AuctionCluster");
        this.dispatcher = new RpcDispatcher(this.channel, this);
    }


    /**
     * Will recieve messages from the servers.
     *
     * @param message
     */
    public void receive(Message message) {

    }

    public void viewAccepted(View view) {

    }

    @Override
    public SignedObject verify(Auth authObject) throws RemoteException, UnknownHostException {
        return null;
    }

    @Override
    public boolean verifyClient(SignedObject signedObject) throws RemoteException {
        return false;
    }

    @Override
    public Auth createClientAuth(Auth authObject) throws RemoteException {
        return null;
    }

    @Override
    public String browseAuctions() throws RemoteException {
        return null;
    }

    @Override
    public String createAuction(Item item, User user) throws RemoteException {
        return null;
    }

    @Override
    public String closeAuction(int auctionId, User user) throws RemoteException {
        return null;
    }

    @Override
    public String bid(int auctionId, BigDecimal amount, User user) throws RemoteException {
        return null;
    }
}
