/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Slave implements Task {

    public Slave() {
    }

    public String doTask(String command) {
        
        StringBuffer output = new StringBuffer();
        
        Process p;
        try {
            p = Runtime.getRuntime().exec(new String[]{"bash","-c",command});
              //p = Runtime.getRuntime().exec(new String[]{"bash","-c",command});
            p.waitFor();
            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();
    }

    
     public boolean writeBytes(byte[] b, String filename) {
        
        try {
             FileOutputStream fos = new FileOutputStream(filename);
             fos.write(b);
             fos.close();
        }
        catch(FileNotFoundException ex)   {
             System.out.println("FileNotFoundException : " + ex);
        }
        catch(IOException ioe)  {
             System.out.println("IOException : " + ioe);
        }
        
        return true;
    }

  
    public static void main(String args[]) {
    
    
        if (args.length < 1 ) {
         System.out.println("expected at least 1 argument Usage: java Slave <slave_id> (<registry_host>)");
        }
        else {
            //  ID to diferentiate different slaves in the registry
            int id = Integer.parseInt(args[0]);
            System.out.println(args.length);
            String host = (args.length == 2) ? args[1] : null;
        
            try {
                Slave obj = new Slave();
                Task stub = (Task) UnicastRemoteObject.exportObject(obj, 0);

                // Retrieves the remote Registry
                Remote r = Naming.lookup("rmi://"+host+"/Master");
                // Binds the Slave
                ((MasterInt) r).register(stub, id);

                System.err.println("Slave ready");
            } catch (Exception e) {
                System.err.println("Slave exception: " + e.toString());
                e.printStackTrace();
            }
        }
    
    }
}
