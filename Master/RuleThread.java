import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


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
                  // Logger.getLogger(RuleThread.class.getName()).log(Level.SEVERE, null, ex);
               }
           }
}

 ArrayList<String> commands = m.getRuleCommands(target);
  for(String c : commands){
  String response = null;
  try{
  //search a free machine
  int freeMachineIndex=-1;
  while(true){
     freeMachineIndex= machines.getAvailableMachine();
     if(freeMachineIndex != -1) break;
  }
  
  //send commands to slave
  System.out.println("Sending to the slave the command " + c);
   Machine machine = machines.machines.get(freeMachineIndex);
    response = machine.doTask(c);
    
    //free the machine
    machines.setFree(freeMachineIndex);
  }catch(RemoteException ex){
     //to do
  }
  System.out.println(response);
  }
  r.state=2;
}
}
