import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public class Client {
    public static void main(String[] args) {
        // Usage : java Client ipMaster
        try {
            String host = args[0];
            Remote r = Naming.lookup("rmi://"+ host + "/Master");
            MasterInt master = (MasterInt) r;
            master.make(host);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
