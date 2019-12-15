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
//import Master.*;

public class Master {

    private Master() {
    }

    public void sendRequest(String s) {

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
    // ArrayList<Rule> rules = m.getRules();
     Integer i;
     for(i=0;i<m.getRulesSize();i++){
    //  ArrayList<String> pre = m.getRules.get(i).getPrerequisites();
       ArrayList<String> pre = m.getRulePrerequisites(i);
      for(String p : pre){
           // stubs[0].receiveFile(p);
        }
         ArrayList<String> commands= m.getRuleCommands(i);
                    for(String c : commands){
                        String response = stubs.get(0).doTask(c);
                        System.out.println(response);
                    }
        
        
     }
    }else{
    
     System.out.println("Couldn't bind all Slaves");
    
    }
    
    
    

      
    }
}
