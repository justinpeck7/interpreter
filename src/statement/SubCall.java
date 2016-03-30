package statement;

import interpreter.Interpreter;

import java.util.ArrayList;
import java.util.List;

import datavalue.DataValue;
import expression.Expression;
import token.Token;
import token.TokenStream;

/**
 * Derived class that represents a subroutine call statement in the SILLY language.
 *   @author Justin Peck
 *   @version 3/29/16
 */
public class SubCall extends Statement {
	private Token name;
	private List<Expression> paramsToBind;
	
    /**
     * Reads in a SubCall statement from the specified stream
     *   @param input the stream to be read from
     */
	public SubCall(TokenStream input) throws Exception {
		this.paramsToBind = new ArrayList<Expression>();
		Token call = input.next();
		this.name = input.next();
		Token delim = input.next();
		
		if(!call.toString().equals("call") || !delim.toString().equals("(") || name.getType() != Token.Type.IDENTIFIER) {
			throw new Exception("SYNTAX ERROR: Malformed call statement");
		}
		
		while (!input.lookAhead().toString().equals(")")) {
			Expression param = new Expression(input);
			this.paramsToBind.add(param);
		}
		
		input.next();		
	}

	/**
	 * Executes the SubCall statement
	 */
	public void execute() throws Exception {
		List<Token> paramNames = Interpreter.MEMORY.getParamsForSub(this.name);
		List<Statement> stmts = Interpreter.MEMORY.getStmtsForSub(name);
		List<DataValue> params = new ArrayList<DataValue>();
		
		if (this.paramsToBind.size() != paramNames.size()) {
			throw new Exception("Incorrect number of params for subroutine: " + name);
		}
		
		for (int i = 0; i < this.paramsToBind.size(); i++) {
			params.add(this.paramsToBind.get(i).evaluate());
		}
		
		Interpreter.MEMORY.createNewScope();
		
		for(int i = 0; i < params.size(); i++) {
			Interpreter.MEMORY.storeOnCurrentScope(paramNames.get(i), params.get(i));
		}
		
		for (int i = 0; i < stmts.size(); i++) {
			stmts.get(i).execute();
		}
		
		Interpreter.MEMORY.destroyScope();
	}

    /**
     * Converts the current SubCall statement into a String.
     *   @return the String representation of this statement
     */
	public String toString() {
		String params = "";
		for (int i = 0; i < this.paramsToBind.size(); i ++) {
			params += this.paramsToBind.get(i) + " ";
		}
		return "call " + this.name.toString() + " ( " + params + ")";
	}
}
