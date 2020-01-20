import java.rmi.RemoteException;
import java.util.ArrayList;



class RuleThread extends Thread {
      
      MakefileClass m;
      MachineStates machines;
      String  target;
      Master master;


RuleThread(String target, MachineStates machines, MakefileClass m, Master master){
    this.target = target;
    this.m = m;
    this.machines = machines;
    this.master = master;
}


@Override
public void run() {

    Rule r = m.getRules().get(target);
    Rule ru;
    System.out.println("Starting RuleThread for rule : " + r.target);

    ArrayList<String> prerequisites = r.getPrerequisites();

    //check for dependencies and run a thread for each dependencie
    if (prerequisites.size() > 0) {

        RuleThread preThreads[] = new RuleThread[prerequisites.size()];

        //a new thread for each dependecy
        for (int i = 0; i < prerequisites.size(); i++) {
            // ru= m.getRules().stream().filter(rule->rule.getTarget().equals(prerequisites.get(i))).findAny();
            if (m.getRules().containsKey(prerequisites.get(i))) {
                // Si la dependance est aussi une cible et non un fichier deja present
                ru = m.getRules().get(prerequisites.get(i));
                // System.out.println("prerequisites.get(i) -> "+prerequisites.get(i));
                // System.out.println("prerequisites.get(i+1) -> "+prerequisites.get(i+1));
                // System.out.println("m.getRules() -> "+m.getRules());
                // System.out.println("object ru->"+ru);
                //synchronised function for mutual exclusion over the same dipendency
                if (ru.getState() == 1) {

                    preThreads[i] = new RuleThread(prerequisites.get(i), machines, m, master);
                    preThreads[i].start();
                }
            }
            else {
                preThreads[i] = null;
            }
        }

        //Synchronisation
        for (RuleThread preThread : preThreads) {
            if (preThread != null) {
                try {
                    preThread.join();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        if( target == m.rootName ) {
            //On a terminé la cible racine, on a finit de make

        }
    }

    ArrayList<String> commands = m.getRuleCommands(target);
    if (commands.size() > 0) {
        //search a free machine
        int freeMachineIndex = -1;
        Machine machine = new Machine();
        while (true) {
            machine = machines.getAvailableMachine();
            if (machine != null) break;
        }
        // Machine machine = machines.freeMachines.get(freeMachineIndex);
        // send the files to the slave if the are any

        for (String pre : prerequisites) {
            try {
                machine.writeBytes(pre);
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        }

        for (String c : commands) {
            String response = null;
            try {


                //send commands to slave
                System.out.println("Sending to the slave the command " + c);

                response = machine.doTask(c);

                //free the machine
                machines.setFree(machine);
            } catch (RemoteException ex) {
                //to do
                ex.printStackTrace();
            }
            System.out.println(response);


        }
        // Retrieve the file resulting from the slave task.

        try {
            machine.receiveFromSlave(this.target);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
        master.endOfMake(target);
    }
    r.state = 2;
}
}
