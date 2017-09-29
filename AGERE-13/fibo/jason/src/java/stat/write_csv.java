// Internal action code for project eval_fib

package stat;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class write_csv extends DefaultInternalAction {

    BufferedWriter out = null;
    
    public write_csv() {
        try {
            out = new BufferedWriter(new FileWriter("data.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        out.write(args[0]+","+args[1]+"\n");
        return true;
    }
    
    @Override
    public void destroy() throws Exception {
        out.close();
    }
}
