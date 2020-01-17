To compile:

	javac -d . `cat src_files`
Start Java RMI registry:

	 rmiregistry&

Start the Slave:

	java Slave <slave_id> [<registry_host_addres>]

Start the Master:

	java Master <number_of_slaves> [<registry_host_addres>]

Connect to grid5000 :

	ssh login@access.grid5000.fr

Réservation de x noeuds en mode interactif :

	oarsh -l nodes=x -I

Réservation de x noeuds avec lancement d'une commande au démarrage :

	oarsh -l nodes=x "./cmd.sh arg1 arg2"
