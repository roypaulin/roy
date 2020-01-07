package com.ensimag.parmake;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MasterTask {

    private ArrayList<String> cibles; // Liste des cibles à traiter
    private ArrayList<String> cmdSend; // Liste des cibles en cours de traitement
    private Map<String, String[]> dependances; // Liste des dépendances de chaque cible
    private Map<String, String> commandes; // Liste des commandes de chaque cible
    private String name;
    private String makefile; // Chemin du Makefile --> Si on fait tout dans le meme repertoire, inutile ?
    private boolean end = false;
    private Master master; // Master ayant instancié cet objet

    /**
     * Constructor
     * @param name Nom de l'objet
     * @param makefile Chemin du makefile à traiter
     * @param master Master appelant ce constructeur
     */
    public MasterTask(String name, String makefile, Master master) {
        this.name = name;
        this.makefile = makefile;
        this.master = master;
    }

    /**
     * Exécution du makefile.
     * Tous les fichiers nécessaires doivent avoir été uploadés d'abord.
     */
    public void run() {
        System.out.println("Début d'exécution du make");
        cibles = new ArrayList<>();
        dependances = new HashMap<>();
        commandes = new HashMap<>();
        // Parsing du fichier
        File file = new File(makefile);
        String lastTarget = null; // Nom de la cible parsée le + récemment
        System.out.println("Parsing du makefile");
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader br=new BufferedReader(fileReader);  //creates a buffering character input stream
            String line;
            while((line=br.readLine())!=null)
            {
                // Parcourt chaque ligne et enregistre les cibles dans un tableau/liste
                String[] lineCut = line.split(" ");
                if(lineCut[0].endsWith(":")) {
                    // On enregistre la cible
                    String cible = lineCut[0].substring(0, lineCut[0].length()-2);
                    cibles.add(cible);
                    lastTarget = cible;
                    // On enregistre les dependances de cette cible
                    ArrayList<String> dependance = new ArrayList<>();
                    for (int i = 1; i < lineCut.length-1; i++ ) {
                        dependance.add(lineCut[i]);
                    }
                    String[] dependanceArr = dependance.toArray(new String[dependance.size()]);
                    dependances.put(cible, dependanceArr);
                }
                else if(lineCut[1].equals(":")) {
                    // On enregistre la cible
                    String cible = lineCut[0];
                    cibles.add(cible);
                    lastTarget = cible;
                    // On enregistre les dependances de cette cible
                    ArrayList<String> dependance = new ArrayList<>();
                    dependance.addAll(Arrays.asList(lineCut).subList(2, lineCut.length - 1));
                    String[] dependanceArr = dependance.toArray(new String[dependance.size()]);
                    dependances.put(cible, dependanceArr);
                }
                else if(!line.isEmpty()) {
                    // On est sur une ligne de commande, on l'enregistre
                    commandes.put(lastTarget, line);
                }
            }
            fileReader.close();    //closes the stream and release the resources
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Exécution du makefile");
        // Exécution du make
        boolean makeable;
        // Tant qu'il reste une cible non resolue
        while(!cibles.isEmpty()) {
            for(String c : cibles) {
                // On parcourt les dépendances de la cible, et on regarde si il y a des cibles non resolues dedans
                String[] dep = dependances.get(c);
                makeable = true;
                for(String d : dep) {
                    if (cibles.contains(d) || cmdSend.contains(d)) {
                        makeable = false;
                        break;
                    }
                }
                if(makeable) {
                    // On transmet les fichiers
                    // On crée une tâche devant executer la commande
                    System.out.println("Exécution de la cible " + c);
                    Task t = new Task(c, commandes.get(c), this, dep, master);
                    t.start();
                    cmdSend.add(c);
                    cibles.remove(c);
                }
            }
        }
        while(!cmdSend.isEmpty()) {
            // On attend le retour de tous les résultats
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // On a reçu le resultat, on peut notifier l'utilisateur.
        System.out.println("Fin d'exécution du make !");
        System.out.println("Vous pouvez récupérer votre fichier en faisant : java LaunchClient download <nom-du-fichier>");
    }

    /**
     * Supprime la cible de la liste des cibles
     * @param cible La cible à retirer
     */
    public void removeCible(String cible) {
        cmdSend.remove(cible);
        dependances.remove(cible);
        commandes.remove(cible);
        System.out.println("Fin d'exécution de la cible " + cible);
    }
}
