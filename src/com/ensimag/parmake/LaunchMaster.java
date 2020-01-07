package com.ensimag.parmake;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class LaunchMaster {

    public static void main(String[] args) {
        //Demarrage du serveur
        try {
            LocateRegistry.createRegistry(1099);

            System.out.println("Mise en place du Security Manager ...");
           /** if (System.getSecurityManager() == null) {
                System.setSecurityManager(new RMISecurityManager());
            }**/

            Master master = new Master();

            String url = "rmi://" + InetAddress.getLocalHost().getHostAddress() + "/Master";
            System.out.println("Enregistrement de l'objet avec l'url : " + url);
            Naming.rebind(url, master);

            System.out.println("Serveur maître lancé");
        } catch (RemoteException | UnknownHostException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
