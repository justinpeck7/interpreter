package statement;

import token.Token;
import token.TokenStream;

/**
 * Derived class that represents the quit statement in the SILLY language.
 *   @author Dave Reed
 *   @version 2/8/16
 */
public class Quit extends Statement {
    /**
     * Reads in a quit statement from the specified input stream
     *   @param input the stream to be read from
     */
    public Quit(TokenStream input) throws Exception {
        Token keyword = input.next();
        if (!keyword.toString().equals("quit")) {
            throw new Exception("SYNTAX ERROR: Malformed quit statement");
        }
    }

    /**
     * Executes the current quit statement.
     */
    public void execute() {
        System.out.println("BYE");
    }

    /**
     * Converts the current quit statement into a String.
     *   @return the String representation of this statement
     */
    public String toString() {
        return "quit";
    }
}