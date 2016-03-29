package statement;

import interpreter.Interpreter;

import java.util.ArrayList;
import java.util.List;

import datavalue.DataValue;
import expression.Expression;
import token.Token;
import token.TokenStream;

public class SubCall extends Statement {
	private Token name;
	private List<DataValue> paramsToBind;
	
	public SubCall(TokenStream input) throws Exception {
		this.paramsToBind = new ArrayList<DataValue>();
		Token call = input.next();
		this.name = input.next();
		Token delim = input.next();
		
		if(!call.toString().equals("call") || !delim.toString().equals("(") || name.getType() != Token.Type.IDENTIFIER) {
			throw new Exception("SYNTAX ERROR: Malformed call statement");
		}
		
		while (!input.lookAhead().toString().equals(")")) {
			DataValue param = new Expression(input).evaluate();
			this.paramsToBind.add(param);
		}
		
		input.next();		
	}

	@Override
	public void execute() throws Exception {
		List<Token> paramNames = Interpreter.MEMORY.getParamsForSub(this.name);
		List<Statement> stmts = Interpreter.MEMORY.getStmtsForSub(name);
		if (this.paramsToBind.size() != paramNames.size()) {
			throw new Exception("Incorrect number of params for subroutine: " + name);
		}
		
		Interpreter.MEMORY.createNewScope();
		
		for (int i = 0; i < this.paramsToBind.size(); i++) {
			Interpreter.MEMORY.storeNewVariable(paramNames.get(i), this.paramsToBind.get(i));
		}
		
		for (int i = 0; i < stmts.size(); i++) {
			stmts.get(i).execute();
		}
		
		Interpreter.MEMORY.destroyScope();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
