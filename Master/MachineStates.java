import java.rmi.RemoteException;








public class MachineStates {




public Machine[] machines;


MachineStates(Machine m){

     this.machine=m;
}


synchronized public int getAvailableMachine() {
        //get a free machine
        for (int i = 0; i < machine.length; i++) {
            if (machine[i].isAvailable) {
                machine[i].isAvailable = false;
                //return the free machine index
                return i;
            }                    
        }

         return -1 //couldn't find a free one
}


  synchronized public boolean getState(int i) {
        return machine[i].isAvailable;
    }
    
    
     synchronized public void setFree(int i) {
        machine[i].isAvailable = true;
    }
}
