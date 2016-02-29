package statement;

import interpreter.Interpreter;

import java.util.ArrayList;
import java.util.List;

import datavalue.StringValue;
import token.Token;
import token.TokenStream;

/**
 * Derived class that represents an variable declaration statement in the SILLY language.
 *   @author Justin Peck
 *   @version 2/28/16
 */
public class VarDeclaration extends Statement {
	private List<Token> variables;
	
    /**
     * Reads in a variable declaration statement from the specified TokenStream
     *   @param input the stream to be read from
     */
	public VarDeclaration(TokenStream input) throws Exception {
		variables = new ArrayList<Token>();
		Token keyword = input.next();
		Token delim = input.next();
        if (!keyword.toString().equals("var") || !delim.toString().equals("(")) {
            throw new Exception("SYNTAX ERROR: Malformed var statement");
        }
        
        while (!input.lookAhead().toString().equals(")")) {
        	Token var = input.next();
        	if(var.getType() == Token.Type.IDENTIFIER) {
        		this.variables.add(var);
        	}
        	else {
        		throw new Exception("Variable declarations must be of type IDENTIFIER");
        	}
        }
        input.next();		
	}

    /**
     * Executes the current variable declaration statement.
     */
	public void execute() throws Exception {
		for (Token var : variables) {
			Interpreter.MEMORY.storeNewVariable(var, new StringValue("null"));
		}		
	}

    /**
     * Converts the current assignment statement into a String.
     *   @return the String representation of this statement
     */
	public String toString() {
		String str = "var (";
			for (Token var : variables) {
				str += var.toString();
			}
		str += ")";
		return str;
	}

}
