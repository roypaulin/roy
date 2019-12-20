import java.util.ArrayList;

public class Rule {
        String target;
        ArrayList< String > prerequisites;
        ArrayList< String > commands;
        
        public Rule(String target) {
            prerequisites = new ArrayList<>();
            commands = new ArrayList<>();
            this.target = target;
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
    };
