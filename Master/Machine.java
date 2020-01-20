import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.io.FileOutputStream;




public class Machine {


  
   public Task t;

 Machine() {
        //isAvailable = true;
    }
 public void setTask(Task t) {
        this.t = t;
    } 
    
      private static byte[] fileToBytes(String path){
    File file = new File(path);

        byte[] b = new byte[(int) file.length()];
        
         try {
              FileInputStream fis = new FileInputStream(file);
              fis.read(b);
            // fis.close();
         } catch (FileNotFoundException e) {
                     System.out.println("File Not Found, sorry.");
                     e.printStackTrace();
         }
         catch (IOException e1) {
                  System.out.println("Error Reading The File.");
                   e1.printStackTrace();
         }
    
       return b;
    
    }
    
    public boolean writeBytes(String filename) throws RemoteException{
    
        return t.writeBytes(fileToBytes(filename), filename);
    }
    public boolean receiveFromSlave (String filename) throws RemoteException {
        byte b[] = t.receiveFromSlave(filename);

        if (b == null)
            return false;

        try {
            FileOutputStream fos = new FileOutputStream(filename);
            fos.write(b);
            fos.flush();
            fos.close();
        } catch (IOException ioe) {

            return false;
        }

        return true;


    }
    
    String doTask(String command) throws RemoteException {
        return t.doTask(command);
    }
    
}
