package com.auction;

import org.jgroups.*;
import org.jgroups.blocks.MethodCall;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.blocks.RpcDispatcher;
import org.jgroups.util.Rsp;
import org.jgroups.util.RspList;

import java.io.*;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.RemoteObject;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author JamesDavies
 * @date 07/12/2016
 * Auctioning System
 */
public class FrontendImpl extends UnicastRemoteObject implements BuyerService, SellerService, Receiver, MembershipListener {

    private ArrayList<ReplicaManager> replicaManagers;
    private Channel channel;
    private RpcDispatcher dispatcher;
    private RequestOptions reqOpts;
    private RspList responseList;
    private List<Object> states;
    private MethodCall methodCall;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private ArrayList<Integer> authenticatedUsers;
    private Signature signature;

    /**
     * @throws Exception
     */
    public FrontendImpl() throws Exception {
        this.replicaManagers = new ArrayList<>();
        this.states = new LinkedList<Object>();
        this.channel = new JChannel();
        this.channel.setReceiver(this);
        this.reqOpts = new RequestOptions(ResponseMode.GET_ALL, 1000);
        this.dispatcher = new RpcDispatcher(this.channel, null, this, this);
        this.channel.connect("AuctionCluster");
        this.channel.setDiscardOwnMessages(true);

        this.authenticatedUsers = new ArrayList<>();

        try {
            this.signature = Signature.getInstance("DSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        this.loadServerKeys();

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

    private void filterResponses(RspList responses) {

        for (int i = 0; i < responses.size(); i++) {
            System.out.println(responses.get(i));
        }


    }


    /**
     * Recieves the Auth object, encrypts it, signs it and returns it.
     *
     * @param authObject
     * @return
     */
    public synchronized SignedObject verify(Auth authObject) throws RemoteException {

        SignedObject obj = null;

        try {

            authObject.setOriginIp(Inet4Address.getLocalHost().toString());
            obj = new SignedObject(authObject, this.privateKey, this.signature);

            return obj;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    /**
     * Verify the client object's signature, store client in
     * the authenticated clients list for future use.
     *
     * @param authObject
     */
    public synchronized boolean verifyClient(SignedObject signedObject) throws RemoteException {

        try {
            // Get the public key of the client
            Auth auth = (Auth) signedObject.getObject();

            // Get the public key for this user
            PublicKey key = loadPublicKey(auth.getUserId());

            if (signedObject.verify(key, this.signature)) {

                // Add to list of authenticated users
                this.authenticatedUsers.add(auth.getUserId());

                System.out.println("[AUTH] User #" + auth.getUserId() + " has been authenticated!");

                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Create a basic auth object which will be signed by the client and
     * returned to the server for verification.
     *
     * @param auth
     * @return
     * @throws RemoteException
     */
    public Auth createClientAuth(Auth auth) throws RemoteException {
        Auth returnObj = auth;
        try {
            returnObj.setValue(String.valueOf(new Random().nextInt(100)));
            returnObj.setOriginIp(Inet4Address.getLocalHost().toString());
            return returnObj;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Load the public key of either a user or the server.
     *
     * @param user
     * @return
     */
    private PublicKey loadPublicKey(int user) {
        DSAPublicKey pk = null;
        String location = (user <= 0) ? "server" : String.valueOf(user);

        try {
            FileInputStream fs = new FileInputStream(new File("./keys/public/" + location + ".key"));
            ObjectInputStream ds = new ObjectInputStream(fs);
            byte[] puKey = (byte[]) ds.readObject();
            fs.close();
            ds.close();
            KeyFactory keyFactory = KeyFactory.getInstance("DSA");
            KeySpec ks = new X509EncodedKeySpec(puKey);
            pk = (DSAPublicKey) keyFactory.generatePublic(ks);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pk;
    }

    /**
     * Load the servers public and private key.
     */
    private void loadServerKeys() {
        try {
            FileInputStream fs = new FileInputStream(new File("./keys/private/server.key"));
            ObjectInputStream ds = new ObjectInputStream(fs);
            byte[] prKey = (byte[]) ds.readObject();
            fs.close();
            ds.close();
            KeyFactory keyFactory = KeyFactory.getInstance("DSA");
            KeySpec ks = new PKCS8EncodedKeySpec(prKey);
            this.privateKey = (DSAPrivateKey) keyFactory.generatePrivate(ks);
            this.publicKey = loadPublicKey(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String browseAuctions() throws Exception {
        this.responseList = this.dispatcher.callRemoteMethods(null, "browseAuctions", new Object[]{}, new Class[]{}, this.reqOpts);
        System.out.println(this.responseList.getResults().get(0));
        return this.responseList.getResults().get(0).toString();
    }

    public String createAuction(Item item, User user) throws Exception {
        this.responseList = this.dispatcher.callRemoteMethods(null, "createAuction", new Object[]{item, user}, new Class[]{Item.class, User.class}, this.reqOpts);
        System.out.println(this.responseList.getResults().get(0));
        return this.responseList.getResults().get(0).toString();
    }

    public String closeAuction(int auctionId, User user) throws Exception {
        this.responseList = this.dispatcher.callRemoteMethods(null, "closeAuction", new Object[]{auctionId, user}, new Class[]{int.class, User.class}, this.reqOpts);

        return this.responseList.getResults().get(0).toString();
    }

    public String bid(int auctionId, BigDecimal amount, User user) throws Exception {
        this.responseList = this.dispatcher.callRemoteMethods(null, "bid", new Object[]{auctionId, amount, user}, new Class[]{int.class, BigDecimal.class, User.class}, this.reqOpts);

        return this.responseList.getResults().get(0).toString();
    }

}
