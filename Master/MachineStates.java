import java.rmi.RemoteException;








public class MachineStates {




public Machine[] machines;


MachineStates(Machine m){

     this.machines=m;
}


synchronized public int getAvailableMachine() {
        //get a free machine
        for (int i = 0; i < machines.length; i++) {
            if (machines[i].isAvailable) {
                machines[i].isAvailable = false;
                //return the free machine index
                return i; 
            }                    
        }

         return -1 //couldn't find a free one
}


  synchronized public boolean getState(int i) {
        return machines[i].isAvailable;
    }
    
    
     synchronized public void setFree(int i) {
        machines[i].isAvailable = true;
    }
}
