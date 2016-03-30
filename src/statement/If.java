package statement;

import interpreter.Interpreter;

import java.util.ArrayList;
import java.util.List;

import datavalue.BooleanValue;
import datavalue.DataValue;
import expression.Expression;
import token.Token;
import token.TokenStream;

/**
 * Derived class that represents an if statement in the SILLY language.
 *   @author Justin Peck
 *   @version 3/29/16
 */
public class If extends Statement {
	private List<List<Statement>> statements;
	private List<Expression> conditions;
	private boolean hasElse = false;
	
    /**
     * Reads in an if statement from the specified stream
     *   @param input the stream to be read from
     */
	public If(TokenStream input) throws Exception {
		statements = new ArrayList<List<Statement>>();
		conditions = new ArrayList<Expression>();
		Token keyword = input.next();
		if (!keyword.toString().equals("if")) {
			throw new Exception("SYNTAX ERROR: Malformed if statement");
		}
		
		List<Statement> currentStmts = new ArrayList<Statement>();
		Expression cond = new Expression(input);
		conditions.add(cond);
		
		while(!input.lookAhead().toString().equals("end")) {
			if (input.lookAhead().toString().equals("elif")) {
				input.next();
				statements.add(currentStmts);
				currentStmts = new ArrayList<Statement>();				
				cond = new Expression(input);
				conditions.add(cond);
			}
			else if (input.lookAhead().toString().equals("else")) {
				this.hasElse = true;
				input.next();
				statements.add(currentStmts);
				currentStmts = new ArrayList<Statement>();
			}
			currentStmts.add(Statement.getStatement(input));
		}
		statements.add(currentStmts);
		input.next();
	}

	 /**
     * Executes the if statement
     */
	public void execute() throws Exception {
		Interpreter.MEMORY.createNewScope();
		int stmtIndex = -1;
		for (int i = 0; i < conditions.size(); i++) {
			DataValue cond = conditions.get(i).evaluate();
			if (cond.getType() == Token.Type.BOOLEAN) {
				if (((BooleanValue) cond).value == true) {
					stmtIndex = i;
					break;
				}
			}
			else {
				throw new Exception("If conditions must evaluate to booleans");
			}
		}
		if (stmtIndex != -1) {
			for(Statement stmt : statements.get(stmtIndex)) {
				stmt.execute();
			}		
		}
		else if (this.hasElse) {
			for(Statement stmt : statements.get(0)) {
				stmt.execute();
			}
		}		
	
		Interpreter.MEMORY.destroyScope();
	}

    /**
     * Converts the current if statement into a String.
     *   @return the String representation of this statement
     */
	public String toString() {
		String str = "if ";
		for (int i = 0; i < this.statements.size(); i++) {
			if (i == 0) {
				str += this.conditions.get(i) + "\n";
				for (int j = 0; j < this.statements.get(i).size(); j++) {
					str += this.statements.get(i).get(j);
				}
			}
			else if (i != 0 && i <= this.conditions.size() - 1) {
				str += "\nelif " + this.conditions.get(i) + "\n";
				for (int j = 0; j < this.statements.get(i).size(); j++) {
					str += this.statements.get(i).get(j);
				}
			}
			else {
				str += "\nelse\n";
				for (int j = 0; j < this.statements.get(i).size(); j++) {
					str += this.statements.get(i).get(j);
				}
			}
		}
		return str + "\nend";
	}
}
