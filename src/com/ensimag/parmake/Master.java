package com.ensimag.parmake;

import java.io.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Master extends UnicastRemoteObject implements MasterInt {
    private int nextId = 0;
    private ArrayList<String> freeSlaves;

    protected Master() throws RemoteException {
        super();
        freeSlaves = new ArrayList<>();
    }

    @Override
    public void register(Slave slave) throws RemoteException {
        Registry r = LocateRegistry.getRegistry(1099);
        r.rebind(slave.getName(), slave);
        freeSlaves.add(slave.getName());
    }

    @Override
    public void unregister(Slave slave) throws RemoteException {
        try {
            Registry r = LocateRegistry.getRegistry(1099);
            r.unbind(slave.getName());
            freeSlaves.remove(slave.getName());
        } catch (NotBoundException e) {
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
    public byte[] downloadFile(String serverpath) throws RemoteException {
        byte [] data;
        File serverpathfile = new File(serverpath);
        data=new byte[(int) serverpathfile.length()];
        FileInputStream in;
        try {
            in = new FileInputStream(serverpathfile);
            in.read(data, 0, data.length);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public void launchMake(String makefile) throws RemoteException {
        MasterTask t = new MasterTask(makefile, makefile, this);
        t.run();
    }

    @Override
    public int getNextId() {
        nextId = nextId + 1;
        return nextId-1;
    }

    /**
     * Sélectionne une machine esclave libre aléatoirement
     * @return Le nom de la machine sélectionnée
     */
    protected String getFreeSlave() {
        Random r = new Random();
        int num = r.nextInt(freeSlaves.size());
        String res= freeSlaves.get(num);
        freeSlaves.remove(num);
        return res;
    }

    /**
     * Libère un esclave
     * @param name
     */
    protected void releaseSlave(String name) {
        freeSlaves.add(name);
    }
}
