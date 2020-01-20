/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Task extends Remote {
    String doTask(String command) throws RemoteException;
     boolean writeBytes(byte[] file, String filename) throws RemoteException;
     byte[] receiveFromSlave(String filename) throws RemoteException;
}
