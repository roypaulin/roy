package com.ensimag.parmake;

import java.io.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class Slave extends UnicastRemoteObject implements SlaveInt {
    private String name;

    /**
     * Constructor
     * @throws RemoteException
     */
    protected Slave() throws RemoteException {
        super();
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public void setName(String name) throws RemoteException {
        this.name = name;
    }

    @Override
    public void executeCmd(String commande, String cible) throws RemoteException {
        try {
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec(commande);
            pr.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        // Quand la commande est finie, on envoie le fichier créé
        try {
            Remote r = Naming.lookup("rmi://10.0.0.13/Master");
            if (r instanceof  MasterInt) {
                Master master = ((Master) r);
                uploadToMaster(master, cible, cible);
            }
        } catch (NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void uploadFile(byte[] data, String serverpath, int length) throws RemoteException {
        try {
            File pathFile = new File(serverpath);
            FileOutputStream out = new FileOutputStream(pathFile);
            byte[] dataIn = data;
            out.write(dataIn);
            out.flush();
            out.close();
            System.out.println("Fichier transféré");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void uploadToMaster(Master master, String clientPath, String masterPath) {
        try {
            File clientPathFile = new File(clientPath);
            byte[] data = new byte[(int) clientPathFile.length()];
            FileInputStream in = new FileInputStream(masterPath);
            in.read(data, 0, data.length);
            master.uploadFile(data, masterPath, (int) clientPathFile.length());
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
