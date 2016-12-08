package com.auction;

import org.jgroups.*;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.blocks.RpcDispatcher;
import org.jgroups.util.RspList;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.RemoteObject;
import java.rmi.server.UnicastRemoteObject;
import java.security.SignedObject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author JamesDavies
 * @date 07/12/2016
 * Auctioning System
 */
public class FrontendImpl extends UnicastRemoteObject implements BuyerService, SellerService, Receiver {

    private ArrayList<ReplicaManager> replicaManagers;
    private Channel channel;
    private RpcDispatcher dispatcher;
    private RequestOptions reqOpts;
    private RspList responseList;
    private List<Object> states;

    /**
     * @throws Exception
     */
    public FrontendImpl() throws Exception {
        this.replicaManagers = new ArrayList<>();
        this.states = new LinkedList<Object>();
        this.channel = new JChannel();
        this.channel.setReceiver(this);
        this.reqOpts = new RequestOptions(ResponseMode.GET_ALL, 5000);
        this.channel.connect("AuctionCluster");
        this.dispatcher = new RpcDispatcher(this.channel, this);
        System.out.println("[FRONTEND] Initialised!");
    }

    /**
     * Will receive messages from the servers.
     *
     * @param message
     */
    public void receive(Message message) {
        System.out.println(message.getObject());

        synchronized (message) {
            this.states.add(message);
        }
    }

    @Override
    public void getState(OutputStream outputStream) throws Exception {

    }

    @Override
    public void setState(InputStream inputStream) throws Exception {

    }

    public void viewAccepted(View view) {

    }

    @Override
    public void suspect(Address address) {

    }

    @Override
    public void block() {

    }

    @Override
    public void unblock() {

    }

    public SignedObject verify(Auth authObject) throws Exception {
        this.responseList = this.dispatcher.callRemoteMethods(null, "verify", new Object[]{authObject}, new Class[]{Auth.class}, this.reqOpts);

        return (SignedObject) this.responseList.get(0).getValue();
    }

    public boolean verifyClient(SignedObject signedObject) throws Exception {
        this.responseList = this.dispatcher.callRemoteMethods(null, "verify", new Object[]{signedObject}, new Class[]{SignedObject.class}, this.reqOpts);

        return (boolean) this.responseList.get(0).getValue();
    }

    public Auth createClientAuth(Auth authObject) throws Exception {
        this.responseList = this.dispatcher.callRemoteMethods(null, "createClientAuth", new Object[]{authObject}, new Class[]{Auth.class}, this.reqOpts);

        return (Auth) this.responseList.get(0).getValue();
    }

    public String browseAuctions() throws Exception {
        this.responseList = this.dispatcher.callRemoteMethods(null, "browseAuctions", new Object[]{}, new Class[]{}, this.reqOpts);

        return (String) this.responseList.get(0).getValue();
    }

    public String createAuction(Item item, User user) throws Exception {
        this.responseList = this.dispatcher.callRemoteMethods(null, "createAuction", new Object[]{item, user}, new Class[]{Item.class, User.class}, this.reqOpts);

        return (String) this.responseList.get(0).getValue();
    }

    public String closeAuction(int auctionId, User user) throws Exception {
        this.responseList = this.dispatcher.callRemoteMethods(null, "closeAuction", new Object[]{auctionId, user}, new Class[]{int.class, User.class}, this.reqOpts);

        return (String) this.responseList.get(0).getValue();
    }

    public String bid(int auctionId, BigDecimal amount, User user) throws Exception {
        this.responseList = this.dispatcher.callRemoteMethods(null, "bid", new Object[]{auctionId, amount, user}, new Class[]{int.class, BigDecimal.class, User.class}, this.reqOpts);

        return (String) this.responseList.get(0).getValue();
    }

}
