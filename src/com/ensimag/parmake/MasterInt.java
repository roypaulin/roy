package com.ensimag.parmake;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface du serveur maître
 */
public interface MasterInt extends Remote {

    /**
     * Fonction appelée par un esclave pour s'enregistrer sur le master.
     * @param slave l'esclave voulant s'enregistrer
     * @throws RemoteException
     */
    void register(Slave slave) throws RemoteException;

    /**
     * Fonction appelée par un esclave pour se désenregistrer du master.
     * @param slave l'esclave se deconnectant
     * @throws RemoteException
     */
    void unregister(Slave slave) throws RemoteException;

    /**
     * Ecriture d'un fichier sur le master
     * @param data Les données du fichier
     * @param serverpath Le chemin où mettre le fichier
     * @param length La taille du fichier
     * @throws RemoteException
     */
    void uploadFile(byte[] data, String serverpath, int length) throws RemoteException;

    /**
     * Récupération d'un fichier sur le master
     * @param serverpath Le chemin où mettre le fichier
     * @throws RemoteException
     */
    byte[] downloadFile(String serverpath) throws RemoteException;

    /**
     * Fonction appelée par un client pour lancer un make.
     * @param makefile Le chemin vers le makefile utilisé (sur le serveur)
     * @throws RemoteException
     */
    void launchMake(String makefile) throws RemoteException;

    /**
     * Fonction renvoyant un id unique pour les machines esclaves
     * @return Un id unique
     * @throws RemoteException
     */
    int getNextId() throws RemoteException;
}
