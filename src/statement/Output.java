package statement;

import java.util.ArrayList;

import expression.Expression;
import token.Token;
import token.TokenStream;

/**
 * Derived class that represents an output statement in the SILLY language.
 *   @author Dave Reed
 *   @version 2/8/16
 */
public class Output extends Statement {
    private ArrayList<Expression> expr;

    /**
     * Reads in an output statement from the specified TokenStream.
     *   @param input the stream to be read from
     */
    public Output(TokenStream input) throws Exception {
        this.expr = new ArrayList<Expression>();

        Token op = input.next();
        Token delim = input.next();
        if (!op.toString().equals("output") || !delim.toString().equals("(")) {
            throw new Exception("SYNTAX ERROR: Malformed output statement");
        }

        while (!input.lookAhead().toString().equals(")")) {
            this.expr.add(new Expression(input));
        }
        delim = input.next();
    }

    /**
     * Executes the current output statement.
     */
    public void execute() throws Exception {
        for (Expression e : this.expr) {
            System.out.print(e.evaluate().getValue() + " ");
        }
        System.out.println();
    }

    /**
     * Converts the current output statement into a String.
     *   @return the String representation of this statement
     */
    public String toString() {
        String outval = "output ( ";
        for (Expression e : this.expr) {
            outval += e + " ";
        }
        return outval + ")";
    }
}