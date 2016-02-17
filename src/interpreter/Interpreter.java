package interpreter;

import memory.MemorySpace;
import statement.Quit;
import statement.Statement;
import token.TokenStream;

/**
 * Main method for the SILLY Interpreter. 
 *   @author Dave Reed 
 *   @version 2/8/16
 */
public class Interpreter {
    public static MemorySpace MEMORY = new MemorySpace();
    
    /** 
     * Main method for starting the SILLY interpreter.
     */
    public static void main(String[] args) throws Exception {        
        TokenStream input;
        if (args.length == 0) {
            input = new TokenStream();
        }
        else {
            input = new TokenStream(args[0]);
        }
        
        Statement stmt;
        do {
            if (args.length == 0) {
                System.out.print(">>> ");
            }
            stmt = Statement.getStatement(input);
            //System.out.println(stmt);
            stmt.execute();
        } while (!(stmt instanceof Quit));

    }
}