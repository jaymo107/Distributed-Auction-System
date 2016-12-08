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
import java.rmi.Remote;
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
public interface FrontendService extends Remote, Service{


    /**
     * Will receive messages from the servers.
     *
     * @param message
     */
    void receive(Message message);

    void getState(OutputStream outputStream) throws Exception;

    void setState(InputStream inputStream) throws Exception;

    void viewAccepted(View view);

    void suspect(Address address);

    void block();

    void unblock();

    SignedObject verify(Auth authObject) throws Exception;

    boolean verifyClient(SignedObject signedObject) throws Exception;

    Auth createClientAuth(Auth authObject) throws Exception;

    String browseAuctions() throws Exception;

    String createAuction(Item item, User user) throws Exception;

    String closeAuction(int auctionId, User user) throws Exception;

    String bid(int auctionId, BigDecimal amount, User user) throws Exception;

}
