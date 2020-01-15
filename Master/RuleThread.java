import java.rmi.RemoteException;
import java.util.ArrayList;



class RuleThread extends Thread {
      
      MakefileClass m;
      MachineStates machines;
      String  target;


RuleThread(String target, MachineStates machines, MakefileClass m){
       this.target = target;
      this.m = m;      
      this.machines = machines;
}


@Override
public void run(){

Rule r = m.getRules().get(target);
 Rule ru;      
       System.out.println("Starting RuleThread for rule : " + r.target);


try {
           Thread.currentThread().sleep(1000);
       } catch (InterruptedException ex) {
           //to do...
           ex.printStackTrace();
       }

     
ArrayList<String> prerequisites = r.getPrerequisites();

//check for dependencies and run a thread for each dependencie
if(prerequisites.size()>0){

RuleThread preThreads[]= new RuleThread[prerequisites.size()];

//a new thread for each dependecy
for (int i = 0; i < prerequisites.size(); i++) {
         // ru= m.getRules().stream().filter(rule->rule.getTarget().equals(prerequisites.get(i))).findAny();
          ru=m.getRules().get(prerequisites.get(i));
          //synchronised function for mutual exclusion over the same dipendency
            if(ru.getState()==1){
         
               preThreads[i] = new RuleThread(prerequisites.get(i), machines, m);
               preThreads[i].start();   
               }
           }
           
    //Synchronisation 
     for (RuleThread preThread : preThreads) {
               try {
                   preThread.join();
               }catch (InterruptedException ex) {
                 ex.printStackTrace();
               }
           }
}

 ArrayList<String> commands = m.getRuleCommands(target);
 if(commands.size()>0){
 //search a free machine
  int freeMachineIndex=-1;
   Machine machine=new Machine();
  while(true){
     machine= machines.getAvailableMachine();
     if(machine != null) break;
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
            
  for(String c : commands){
  String response = null;
  try{
  
  
  //send commands to slave
  System.out.println("Sending to the slave the command " + c);
  
    response = machine.doTask(c);
    
    //free the machine
    machines.setFree(machine);
  }catch(RemoteException ex){
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
  }
  r.state=2;
}
}
