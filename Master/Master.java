/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.NotBoundException;

public class Master {

    private Master() {
    }

    public void sendRequest(String s) {

    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
    
    
    if (args.length < 1) {
            System.out.println("expected at least 1 argument Usage: java Master <#slaves> (<registry_host>)");
        }
        else {
        int slaves = Integer.parseInt(args[0]);
         String host = (args.length == 2) ? args[1] : null;
         
         // Retrieves the Registry and the slaves
            Registry registry = LocateRegistry.getRegistry(host);
            Task stubs[] = new Task[slaves];
            
             for (int i = 0; i < slaves; i++) {
                try {
                    stubs[i] = (Task) registry.lookup("slave" + i);
                } catch (NotBoundException e) {
                    System.err.println
                    ("Slave " + i + " not bound.\n");
                    allOk = false;
                }
        } 
    
    
    }
    
    
    
    
    

      
    }
}
