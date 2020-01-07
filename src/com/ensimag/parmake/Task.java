package com.ensimag.parmake;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Task implements Runnable {
    private Thread t;
    private String taskName;
    private String commande;
    private MasterTask asker;
    private String[] dependances;
    private Master master;

    /**
     * Constructor
     * @param taskName
     * @param commande
     * @param asker
     * @param dependeances
     * @param master
     */
    public Task(String taskName, String commande, MasterTask asker, String[] dependeances, Master master) {
        this.taskName = taskName;
        this.commande = commande;
        this.asker = asker;
        this.dependances = dependeances;
        this.master = master;
    }

    @Override
    public void run() {
        try {
            // On choisit un serveur libre
            Registry reg = LocateRegistry.getRegistry("rmi://10.0.0.13/Master", 1099);
            String slaveName = master.getFreeSlave();
            Slave slave = (Slave) reg.lookup(slaveName);
            // Copie les fichiers
            for( String dep : dependances) {
                uploadToSlave(slave, dep, dep);
            }
            // Exécute la commande
            slave.executeCmd(this.commande, taskName);
            // Le résultat se trouve dans .
            // Notifie le master qu'on a fini l'exécution
            asker.removeCible(taskName);
            master.releaseSlave(slaveName);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lance un nouveau thread exécutant la tâche
     */
    public void start() {
        if (t == null) {
            t = new Thread(this, taskName);
            t.start();
        }
    }

    /**
     * Setteur de la commande
     * @param commande La commande à exécuter
     */
    public void setCommande(String commande) {
        this.commande = commande;
    }

    /**
     * Envoie un fichier vers un esclave
     * @param slave L'esclave à qui envoyer le fichier
     * @param masterPath Le chemin du fichier à envoyer
     * @param slavePath Le chemin où mettre le fichier
     */
    private void uploadToSlave(Slave slave, String masterPath, String slavePath) {
        try {
            File masterPathFile = new File(masterPath);
            byte[] data = new byte[(int) masterPathFile.length()];
            FileInputStream in = new FileInputStream(masterPath);
            in.read(data, 0, data.length);
            slave.uploadFile(data, slavePath, (int) masterPathFile.length());
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
