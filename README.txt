To compile:

	javac -d . Master/Master.java Slave/Slave.java Slave/Task.java Master/MakefileStruct.java
Start Java RMI registry:

	 rmiregistry <PortNumber>

Start the Slave:

	java Slave

Start the Master:

	java   Master
