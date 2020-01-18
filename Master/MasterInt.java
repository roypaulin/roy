import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MasterInt extends Remote {
    public void register(Task slave, int id) throws RemoteException;
    public void make(String host) throws IOException;
}
