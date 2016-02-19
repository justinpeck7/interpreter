package statement;

import java.util.ArrayList;

import expression.Expression;
import token.Token;
import token.TokenStream;

/**
 * Derived class that represents a repeat statement in the SILLY language.
 *   @author Dave Reed
 *   @version 2/8/16
 */
public class Repeat extends Statement {
    private Expression expr;
    private ArrayList<Statement> stmts;  
    
    /**
     * Reads in a repeat statement from the specified stream
     *   @param input the stream to be read from
     */
    public Repeat(TokenStream input) throws Exception {
        Token keyword = input.next();
        if (!keyword.toString().equals("repeat")) {
            throw new Exception("SYNTAX ERROR: Malformed repeat statement");
        }

        this.expr = new Expression(input);
        this.stmts = new ArrayList<Statement>();

        while (!input.lookAhead().toString().equals("end")) {
            this.stmts.add(Statement.getStatement(input));
        }
        input.next();
    }

    /**
     * Executes the current repeat statement.
     */
    public void execute() throws Exception {
        Object val = this.expr.evaluate().getValue();
        if (val instanceof Integer) {
            int numReps = ((Integer) val).intValue();
            for (int i = 0; i < numReps; i++) {
                for (Statement stmt : this.stmts) {
                    stmt.execute();
                }
            }
        } else {
            throw new Exception("RUNTIME ERROR: Repeat expression must be an integer");
        }
    }

    /**
     * Converts the current repeat statement into a String.
     *   @return the String representation of this statement
     */
    public String toString() {
        String str = "repeat " + this.expr + "\n";
        for (Statement stmt : this.stmts) {
            str += "    " + stmt + "\n";
        }
        return str + "end";
    }
}