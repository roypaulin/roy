To compile:

	javac -d . `cat src_files`
Start Java RMI registry:

	 rmiregistry&

Start the Slave:

	java Slave <slave_id> [<registry_host_addres>]

Start the Master:

	java Master <number_of_slaves> [<registry_host_addres>]
