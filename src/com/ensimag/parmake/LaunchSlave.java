package com.ensimag.parmake;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.*;
import java.net.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LaunchSlave {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);

            System.out.println("Mise en place du Security Manager ...");
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new RMISecurityManager());
            }
            // Creation de l'esclave
            Slave slave = new Slave();
            // Enregistrement de l'esclave sur le master
            Remote r = Naming.lookup("rmi://10.0.0.13/Master");
            if (r instanceof  MasterInt) {
                int id = ((MasterInt) r).getNextId();
                String name = String.valueOf(id);
                slave.setName(name);
                ((MasterInt) r).register(slave);
            }
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
