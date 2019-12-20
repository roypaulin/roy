/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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

public class Master {

    private Master() {
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
    
    
    private static void execMakefile(MakefileClass m, ArrayList<Task> stubs) throws RemoteException{
    
     ArrayList<Rule> rules = m.getRules();
   // System.out.println(rules);
    //Execute rules from bottom to top
    for(int i=m.getRulesSize()-1;i>0;i--){
    
    ArrayList<String> pre = rules.get(i).getPrerequisites();
    System.out.println(pre);
    for(String p : pre){
       //send file from prerequisites
       System.out.println("Sending file " + p);
                stubs.get(0).writeBytes(fileToBytes(p), p);
           // stubs[0].receiveFile(p);
        }
        
        // Sends the rule's commands to the slave
         ArrayList<String> commands= m.getRuleCommands(i);
         System.out.println(commands);
         for(String c : commands){
         
          System.out.println(c);
                        String response = stubs.get(0).doTask(c);
                        System.out.println(response);
                    }
        
    }
    
    }
    public static void main(String[] args) throws FileNotFoundException, IOException {
    
    boolean complete=true;
    ArrayList<Task> stubs=new ArrayList<Task>();
    if (args.length < 1) {
            System.out.println("expected at least 1 argument Usage: java Master <#slaves> (<registry_host>)");
        }
        else {
        int slaves = Integer.parseInt(args[0]);
         String host = (args.length == 2) ? args[1] : null;
         
         // Retrieves the Registry and the slaves
            Registry registry = LocateRegistry.getRegistry(host);
           // stubs = new Task[slaves];
            
             for (int i = 0; i < slaves; i++) {
                try {
                    stubs.add((Task) registry.lookup("slave" + i));
                } catch (NotBoundException e) {
                    System.err.println
                    ("Slave " + i + " not bound.\n");
                    complete = false;
                }
        } 
    
    
    }
    
    if(complete){
    
     System.out.println("All slaves bounded successfully.");
     MakefileClass m = new MakefileClass("./Master/makefiles/premier/Makefile");
     m.display();
    // ArrayList<Rule> rules = m.getRules();
    execMakefile(m,stubs);
     
    }else{
    
     System.out.println("Couldn't bind all Slaves");
    
    }
    
    
    

      
    }
}
