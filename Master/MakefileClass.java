import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*
 * this class represent the implementation of a Makefile structure
 * which is composed of a set of rules.
 * Each rule has a set of prerequisites and commands
 
*/

public final class MakefileClass {


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
       
       //set of rules
        private ArrayList< Rule > rules;
        
        public MakefileClass() { 
        rules = new ArrayList<>();
    }
    
    
    
     public MakefileClass(String path) throws FileNotFoundException, IOException
    {
        this();
        
        FileReader f = new FileReader(path);
        BufferedReader b = new BufferedReader(f);
        
        // Reads the file line by line  
        String s;
        int index=-1;
        while (true) {    
         
            if ((s = b.readLine()) == null)
                break;
            
            if(s.length()>0) {
                // Split the rule to separe the taeget from the rest 
                String[] parts = s.split("[:]+");
                
                // the file has prerequisites
                if (parts.length > 1) {
                    index++;
                    
                    //System.out.println("got a rule with dependencies:\nrule: " + tokens[0]);
                    String target = parts[0];

                    // Split prerequisites
                    String prerequisites[] = parts[1].split("[ ]+");
                    ArrayList<String> prerequisite = new ArrayList<>();
                    for (String p : prerequisites) {
                        if (p.length() > 0) {
                            prerequisite.add(p);
                        }
                    }
                    
                     this.insertRule(target, prerequisite);
                }
                else {
                    // CASE: this line is a command
                    if (s.charAt(0)=='\t') {
                        //System.out.println("got command:\n" + s);
                        addCommand(index, s);
                    }
                    // CASE: this line is a rule with no dependencies at all
                    else {
                        //System.out.println("got a rule with no dependencies:\n" + s);
                        index++;
                        this.addRule(s);
                    }
                }

                //System.out.println();
                
           }// end if
            
        }// end while
    
    }// end function

    
    
    
    public void addCommand(int index, String c) {
        rules.get(index).commands.add(c);
    }
    public void addRule(String name) {
        rules.add(new Rule(name));
    }
    
    public void addRule(String name, ArrayList<String> dependencies, ArrayList<String> commands) {
        rules.add(new Rule(name, dependencies, commands));
     }
     
         public void insertRule(String name, ArrayList<String> dependencies) {
        rules.add(new Rule(name, dependencies, new ArrayList<String>()));
    }
    
     public String getRuleTarget(int index) {
        return rules.get(index).target;
    }
    
    public ArrayList<String> getRuleCommands(int index) {
        return rules.get(index).commands;
    }
    
    public ArrayList<String> getRulePrerequisites(int index) {
        return rules.get(index).prerequisites;
    }
    
     public void addPrerequisite(int index, String d) {
        rules.get(index).prerequisites.add(d);
    }
    
    public void setCommands(int index, ArrayList<String> commands) {
        rules.get(index).commands = commands;
    }
    
    public void setPrerequisites(int index, ArrayList<String> prerequisites) {
        rules.get(index).prerequisites = prerequisites;
    }
    
    public int getRulesSize() {
        return rules.size();
    }
    public ArrayList<Rule> getRules() {
        return rules;
    }
};
