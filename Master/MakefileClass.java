import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
/*
 * this class represent the implementation of a Makefile structure
 * which is composed of a set of rules.
 * Each rule has a set of prerequisites and commands
 
*/

public final class MakefileClass {


       
       //set of rules
       // private ArrayList< Rule > rules;
        private Map< String,Rule > rules;
        public String rootName;
         private static String currentRule;
        //public boolean found;
        public MakefileClass() { 
      rules= new HashMap<>();
    }
    
    
    
     public MakefileClass(String path) throws FileNotFoundException, IOException
    {
        this();
        
        FileReader f = new FileReader(path);
        BufferedReader b = new BufferedReader(f);
        boolean found=false;
        // Reads the file line by line  
        String s;
        int index=-1;
        //String currentRule;
        while (true) {    
         
            if ((s = b.readLine()) == null)
                break;
            
            if(s.length()>0) {
                // Split the rule to separe the target from the rest
                String[] parts = s.split("[:]+");

                parts[0] = parts[0].replaceFirst("\\s++$", "");
                String target = parts[0];
                // the file has prerequisites
                if (parts.length > 1) {
                    index++;
                    
                    //System.out.println("got a rule with dependencies:\nrule: " + tokens[0]);
                     currentRule=target;
                    // Split prerequisites
                    String[] prerequisites = parts[1].split("\t|[ ]+");
                    ArrayList<String> prerequisite = new ArrayList<>();
                    for (String p : prerequisites) {
                        if (p.length() > 0) {
                            prerequisite.add(p);
                        }
                    }
                    
                     this.insertRule(target, prerequisite);
                     if(!found){
                     rootName=target;
                     found=true;
                     }
                }
                else {
                    // CASE: this line is a command
                    if (s.charAt(0)=='\t') {
                        while (s.charAt(0) == '\t') {
                            s = s.substring(1);
                        }
                        //System.out.println("got command:\n" + s);
                        addCommand(currentRule, s);
                    }
                    // CASE: this line is a rule with no dependencies at all
                    else {
                        //System.out.println("got a rule with no dependencies:\n" + s);
                        //index++;
                        currentRule=parts[0];
                        this.addRule(currentRule);
                    }
                }

                //System.out.println();
                
           }// end if
            
        }// end while
    
    }// end function

    
    
    
    public void display() {
        System.out.println("Printing makefile structure: (" + this.getRulesSize() + ")");
        
        //for (int r=0; r < this.getRulesSize(); r++) {
        int r=0;
        for(String target : this.getRules().keySet()){
            // Print rule name
            System.out.println("Rule #" + r + ": " + target);
            
            // Prints list of dependencies
            ArrayList<String> pres = this.getRulePrerequisites(target);
            if (pres.size()>0) {
                System.out.print("Dependencies (" + pres.size() + ") : ");
                for (int i = 0; i < pres.size(); i++) {
                    System.out.print(pres.get(i) + ", ");
                }
                System.out.println();
            }
            
            // Prints list of commands
            ArrayList<String> coms = this.getRuleCommands(target);
            System.out.println("Commands (" + coms.size() + ") : ");
            for (int i = 0; i < coms.size(); i++) {
                System.out.println("#" + i + ": " + coms.get(i));           
            }
            r++;
        }
    }
    
    
    
    
    
    
    
    public void addCommand(String rule, String c) {
        rules.get(rule).commands.add(c);
    }
    public void addRule(String name) {
        rules.put(name,new Rule(name));
    }
    
    public void addRule(String name, ArrayList<String> dependencies, ArrayList<String> commands) {
        rules.put(name,new Rule(name, dependencies, commands));
     }
     
         public void insertRule(String name, ArrayList<String> dependencies) {
        rules.put(name,new Rule(name, dependencies, new ArrayList<String>()));
    }
    
     public String getRuleTarget(String rule) {
        return rules.get(rule).target;
    }
    
    public ArrayList<String> getRuleCommands(String rule) {
        return rules.get(rule).commands;
    }
    
    public ArrayList<String> getRulePrerequisites(String rule) { 
       //System.out.println(rules.get(rule).getPrerequisites());
        return rules.get(rule).getPrerequisites();
    }
    
     public void addPrerequisite(String rule, String d) {
        rules.get(rule).prerequisites.add(d);
    }
    
    public void setCommands(String rule, ArrayList<String> commands) {
        rules.get(rule).setCommands(commands);
    }
    
    public void setPrerequisites(String rule, ArrayList<String> prerequisites) {
        rules.get(rule).setPrerequisites(prerequisites);
    }
    
    public int getRulesSize() {
        return rules.size();
    }
  public Map<String,Rule> getRules() {
        return rules;
    }
};
