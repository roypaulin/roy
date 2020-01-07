package com.ensimag.parmake;

import java.io.*;
import java.net.MalformedURLException;
import java.rmi.*;

public class Client {

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

    public void downloadFromMaster(Master master, String masterPath, String clientPath) {
        byte [] mydata;
        try {
            mydata = master.downloadFile(masterPath);
            System.out.println("downloading...");
            File clientpathfile = new File(clientPath);
            FileOutputStream out=new FileOutputStream(clientpathfile);
            out.write(mydata);
            out.flush();
            out.close();
        } catch (RemoteException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
