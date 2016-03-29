package statement;

import interpreter.Interpreter;

import java.util.ArrayList;
import java.util.List;

import token.Token;
import token.TokenStream;

public class SubDeclaration extends Statement {
	private List<Statement> stmts;
	private List<Token> params;
	private Token name;
	
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

	@Override
	public void execute() {
		Interpreter.MEMORY.storeNewSubroutine(this.name, this.stmts, this.params);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
}
