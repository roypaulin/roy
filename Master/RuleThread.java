import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


class RuleThread extends Thread {
      
      MakefileClass m;
      MachineStates machines;
      int ruleId;


RuleThread(int ruleId, MachineStates machines, MakefileClass m){
       this.ruleId = ruleId;
      this.m = m;      
      this.machines = machines;
}


@Override
public void run(){

Rule r = m.getRules().get(ruleId);
       
       System.out.println("Starting RuleThread for rule : " + r.target);


try {
           Thread.currentThread().sleep(1000);
       } catch (InterruptedException ex) {
           //to do...
       }

       int index=ruleId+1;
ArrayList<String> prerequisites = r.getPrerequisites();

//check for dependencies and run a thread for each dependencie
if(prerequisites.size()>0){

RuleThread preThreads[]= new RuleThread[prerequisites.size()];

//a new thread for each dependecy
for (int i = 0; i < prerequisites.size(); i++) {
               preThreads[i] = new RuleThread(index++, machines, m);
               preThreads[i].start();               
           }
           
    //TO BE CONTINUED 
     for (RuleThread preThread : preThreads) {
               try {
                   preThread.join();
               }catch (InterruptedException ex) {
                  // Logger.getLogger(RuleThread.class.getName()).log(Level.SEVERE, null, ex);
               }
           }
}

 ArrayList<String> commands = m.getRuleCommands(ruleId);
  for(String c : commands){
  String response = null;
  try{
  //search a free machine
  int freeMachineIndex=-1;
  while(true){
     freeMachineIndex= machines.getAvailableMachine();
     if(freeMachineIndex != -1) break;
  }
  
  //send commands
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
}
}
