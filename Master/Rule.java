import java.util.ArrayList;

public class Rule {
        String target;
        ArrayList< String > prerequisites;
        ArrayList< String > commands;
        public int state; // 0:not taken and not done 
                          // 1:taken and not done 
                          // 2:taken and done
        
        public Rule(String target) {
            prerequisites = new ArrayList<>();
            commands = new ArrayList<>();
            this.target = target;
            state=0;
        }
        
        public Rule(String target, ArrayList<String> prerequisites, ArrayList<String> commands) {
            this(target);
            this.prerequisites = prerequisites;
            this.commands = commands;
        }
        
        public ArrayList<String> getPrerequisites() {
            return prerequisites;
        }
        public int getPrereqsSize() { return prerequisites.size(); }
        public int getCommandsSize() { return commands.size(); }
        public ArrayList<String> getCommands() {
        return commands;
    } 
      public void setPrerequisites(ArrayList<String> prerequisites){
      this.prerequisites=prerequisites;
      }
      //synchronised function to chandle the choice of a dipendency
      synchronized public int getState(){
      
      if(state==0) state=1;
      return state;
      }
      public void setCommands(ArrayList<String> commands){
      this.commands=commands;
      }
      public String getTarget() {
        return target;
    }

    public void setTtarget(String target) {
        this.target = target;
    }
    };
