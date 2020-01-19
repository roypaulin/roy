/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

//import Master.*;

public class Master extends UnicastRemoteObject implements  MasterInt {

    ArrayList<Machine> machines;
    long startTime;
    public Master() throws RemoteException {
        super();
        machines=new ArrayList<Machine>();
    }

    public void sendRequest(String s) {

    }

    private static byte[] fileToBytes(String path){
    File file = new File(path);

        byte[] b = new byte[(int) file.length()];
        
         try {
              FileInputStream fis = new FileInputStream(file);
              fis.read(b);
            // fis.close();
         } catch (FileNotFoundException e) {
                     System.out.println("File Not Found.");
                     e.printStackTrace();
         }
         catch (IOException e1) {
                  System.out.println("Error Reading The File.");
                   e1.printStackTrace();
         }
    
       return b;
    
    }
    
    
    private void execMakefile(MakefileClass m, MachineStates ms) throws RemoteException{
    
     RuleThread mainThread = new RuleThread(m.rootName,ms,m, this);
     mainThread.start();
    
    
    }

    public void endOfMake(String target) {
        long endTime = System.currentTimeMillis();
        System.out.println("Execution time of target " + target + " : " + (endTime - startTime) + " milliseconds");
    }

    public void register(Task slave, int id) throws RemoteException {
        Registry r = LocateRegistry.getRegistry();
        r.rebind("slave" + id, slave);
        machines.add(new Machine());
    }

    public void make(String host) throws IOException {
        // Retrieves the Registry and the slaves
        //Machine machines[] = new Machine[slaves];
        boolean complete=true;
        Registry registry = LocateRegistry.getRegistry(host);
        // stubs = new Task[slaves];
        int i = 0;
        for (Machine m : machines) {
            try {
                m.setTask((Task) registry.lookup("slave" + i));
                i++;
                // stubs.add((Task) registry.lookup("slave" + i));
            } catch (NotBoundException e) {
                System.err.println
                        ("Slave " + i + " not bound.\n");
                complete = false;
            }
        }
        if(complete){
            System.out.println("All slaves bounded successfully.");
            System.out.println("Start make");
            startTime = System.currentTimeMillis();
            MachineStates ms =new MachineStates(machines);
            MakefileClass m = new MakefileClass("./Makefile");
            m.display();
            // ArrayList<Rule> rules = m.getRules();
            execMakefile(m,ms);
        }else{

            System.out.println("Couldn't bind all Slaves");

        }
    }
}
