import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class LaunchMaster {
    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
        boolean complete = true;
        Master master = new Master();
        if (args.length < 1) {
            System.out.println("expected at least 1 argument Usage: java LaunchMaster <registry_host> #slaves");
        } else {
            String host = args[0];
            String url = "rmi://" + host + "/Master";
            Naming.rebind(url, master);
            System.out.println("Serveur maitre lance");
            System.out.println("En attente de connection des esclaves...");
        }
    }
}
