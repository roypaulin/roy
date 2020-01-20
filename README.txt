To compile:

	javac -d . `cat src_files`
	
Start Java RMI registry:

	 rmiregistry&

Start the Slave:

	java Slave <slave_id> <registry_host_addres>

Start the Master:

	java LaunchMaster <master_ip>
	
Launch the make (can be executed on a different machine than the master) :

    java Client <registry_host_addres>

Connect to grid5000 :

	ssh login@access.grid5000.fr

Réservation de x noeuds en mode interactif :

	oarsub -l nodes=x -I

Réservation de x noeuds avec lancement d'une commande au démarrage :

	oarsh -l nodes=x "./cmd.sh arg1 arg2"

Changement de node au cours d'une réservation :

    oarsh node
    
Pour connaitre le nom des noeuds réservés :

    uniq $OAR_NODEFILE
    
Pour connaitre l'IP d'une machine :

    hostname -I