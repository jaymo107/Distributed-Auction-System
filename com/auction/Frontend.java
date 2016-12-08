package com.auction;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author JamesDavies
 * @date 07/12/2016
 * Auctioning System
 */
public class Frontend implements Remote{

    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.createRegistry(1099);

        FrontendImpl frontend = new FrontendImpl();

        registry.rebind("rmi://localhost/Frontend", frontend);

    }

}
