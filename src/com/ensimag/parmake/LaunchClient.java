package com.ensimag.parmake;

import java.net.MalformedURLException;
import java.rmi.*;

public class LaunchClient {
    public static void main(String[] args) {
        Client c = new Client();
        /**if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }**/
        try {
            Remote r = Naming.lookup("rmi://10.0.0.13/Master");
            Master master = (Master) r;
            if(args[0].equals("upload")) {
                String clientPath = args[1];
                String masterPath = args[2];
                c.uploadToMaster(master, clientPath, masterPath);
            }
            if (args[0].equals("download")) {
                String masterPath = args[1];
                String clientPath = args[2];
                c.downloadFromMaster(master, masterPath, clientPath);
            }
            if (args[0].equals("make")) {
                String makefile = args[1];
                master.launchMake(makefile);
            }
        } catch (NotBoundException | RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
