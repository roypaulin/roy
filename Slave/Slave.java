/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.*;
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

    
     public boolean writeBytes(byte[] data, String filename) {
        
        try {
             FileOutputStream fos = new FileOutputStream(filename);
             byte[] b = data;
             fos.write(b);
             fos.flush();
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

    @Override
    public byte[] receiveFromSlave(String filename) {
        byte [] data;
        File serverpathfile = new File(filename);
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

  
    public static void main(String args[]) {
    
    
        if (args.length < 1 ) {
         System.out.println("expected at least 1 argument Usage: java Slave <slave_id> (<registry_host>)");
        }
        else {
            //  ID to diferentiate different slaves in the registry
            int id = Integer.parseInt(args[0]);
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
