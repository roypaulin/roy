package com.ensimag.parmake;

import java.rmi.*;

public interface SlaveInt extends Remote {

    /**
     * Getteur du nom de l'esclave
     * @return Le nom de l'esclave
     * @throws RemoteException
     */
    String getName() throws RemoteException;

    /**
     * Setteur du nom de l'esclave
     * @param name Le nom de l'esclave
     * @throws RemoteException
     */
    void setName(String name) throws RemoteException;

    /**
     * Exécute une commande
     * @param commande La commande à exécuter
     * @param cible Le nom de la cible (et donc du fichier créé)
     * @throws RemoteException
     */
    void executeCmd(String commande, String cible) throws RemoteException;

    /**
     * Ecriture d'un fichier sur l'esclave
     * @param data Les données du fichier
     * @param serverpath Le chemin où mettre le fichier
     * @param length La taille du fichier
     * @throws RemoteException
     */
    void uploadFile(byte[] data, String serverpath, int length) throws RemoteException;

    /**
     * Envoi d'un fichier vers le master
     * @param master Le serveur maître
     * @param clientPath Le chemin où se situe le fichier
     * @param masterPath Le chemin où mettre le fichier
     */
    void uploadToMaster(Master master, String clientPath, String masterPath);
}
