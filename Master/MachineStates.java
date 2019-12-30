import java.rmi.RemoteException;
import java.util.ArrayList;







public class MachineStates {




public ArrayList<Machine> machines;


MachineStates(ArrayList<Machine> m){

     this.machines=new ArrayList<Machine>(m);
}


synchronized public int getAvailableMachine() {
        //get a free machine
        for (int i = 0; i < machines.size(); i++) {
            if (machines.get(i).isAvailable) {
                machines.get(i).isAvailable = false;
                //return the free machine index
                return i; 
            }                    
        }

         return -1; //couldn't find a free one
}


  synchronized public boolean getState(int i) {
        return machines.get(i).isAvailable;
    }
    
    
     synchronized public void setFree(int i) {
        machines.get(i).isAvailable = true;
    }
}
