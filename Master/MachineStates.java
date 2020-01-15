import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;






public class MachineStates {




//public ArrayList<Machine> machines;
public ArrayList<Machine> freeMachines;

MachineStates(ArrayList<Machine> m){

     //this.machines=new ArrayList<Machine>(m);
     this.freeMachines=new ArrayList<Machine>(m);
}


synchronized public Machine getAvailableMachine() {
        //get a free machine
        if(freeMachines.size()>0){
        Random r = new Random();
        int num = r.nextInt(freeMachines.size());
        Machine m=freeMachines.get(num);
        freeMachines.remove(num);
        //return the free machine index
        return m;
        }
        /*for (int i = 0; i < machines.size(); i++) {
            if (machines.get(i).isAvailable) {
                machines.get(i).isAvailable = false;
                return i; 
            }                    
        }*/

         return null; //couldn't find a free one
}


  
    
     synchronized public void setFree(Machine m) {
     freeMachines.add(m);
        //machines.get(i).isAvailable = true;
    }
}
