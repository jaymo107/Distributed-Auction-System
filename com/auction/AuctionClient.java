package com.auction;

import java.rmi.RemoteException;

public interface AuctionClient {

    /**
     * Get the input from the CLI.
     * @throws RemoteException
     */
    void getInput() throws Exception;
}
