package statement;

import interpreter.Interpreter;

import java.util.ArrayList;
import java.util.List;

import token.Token;
import token.TokenStream;

/**
 * Derived class that represents a subroutine declaration statement in the SILLY language.
 *   @author Justin Peck
 *   @version 3/29/16
 */
public class SubDeclaration extends Statement {
	private List<Statement> stmts;
	private List<Token> params;
	private Token name;
	
    /**
     * Reads in a SubDeclaration statement from the specified stream
     *   @param input the stream to be read from
     */
	public SubDeclaration(TokenStream input) throws Exception {
		this.stmts = new ArrayList<Statement>();
		this.params = new ArrayList<>();
		Token sub = input.next();
		this.name = input.next();
		Token delim = input.next();
		
		if(!sub.toString().equals("sub") || !delim.toString().equals("(") || name.getType() != Token.Type.IDENTIFIER) {
			throw new Exception("SYNTAX ERROR: Malformed sub statement");
		}
		while (!input.lookAhead().toString().equals(")")) { 
			Token param = input.next();
			if(param.getType() != Token.Type.IDENTIFIER) {
				throw new Exception("SYNTAX ERROR: Subroutine parameters must be IDENTIFIERS");
			}
			this.params.add(param);
		}
		
		input.next();
		
		while (!input.lookAhead().toString().equals("end")) {
			this.stmts.add(Statement.getStatement(input));
		}
		
		input.next();
	}

	/**
	 * Executes the SubDeclaration statement
	 */
	public void execute() {
		Interpreter.MEMORY.storeNewSubroutine(this.name, this.stmts, this.params);
	}

    /**
     * Converts the current SubDeclaration statement into a String.
     *   @return the String representation of this statement
     */
	public String toString() {
		String paramStr = "";
		String stmtStr = "";
		for (int i = 0; i < this.params.size(); i ++) {
			paramStr += this.params.get(i) + " ";
		}
		for (int i = 0; i < this.stmts.size(); i++) {
			stmtStr += this.stmts.get(i) + "\n";
		}
		return "sub " + this.name + " ( " + paramStr + ")\n" + stmtStr + "end";
	}
}
