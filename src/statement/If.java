package statement;

import java.util.ArrayList;

import datavalue.BooleanValue;
import expression.Expression;
import token.Token;
import token.TokenStream;

public class If extends Statement {
	private ArrayList<Statement> stmts;
	private Object condition;
	private boolean conditionMet = false;

	public If(TokenStream input) throws Exception {
		Token keyword = input.next();

		if (!keyword.toString().equals("if")) {
			throw new Exception("SYNTAX ERROR: Malformed if statement");
		}
		
		this.getNextCondition(input);
		this.stmts = new ArrayList<Statement>();

		if (conditionIsTrue()) {
			this.conditionMet = true;
			this.getStatementsForIf(input);
			this.continueToEnd(input);
		} else {
			while (!this.conditionMet) {
				while (!input.lookAhead().toString().equals("elif")
						&& !input.lookAhead().toString().equals("else")
						&& !input.lookAhead().toString().equals("end")) {
					input.next();
				}

				if (input.lookAhead().toString().equals("elif")) {
					input.next();
					this.getNextCondition(input);
					if (conditionIsTrue()) {
						this.conditionMet = true;
						this.getStatementsForIf(input);
						this.continueToEnd(input);
					}
				} else if (input.lookAhead().toString().equals("else")) {
					input.next();
					this.conditionMet = true;
					this.getStatementsForIf(input);
					if (!input.lookAhead().toString().equals("end")) {
						throw new Exception(
								"SYNTAX ERROR: Malformed if statement");
					}
				} else if (input.lookAhead().toString().equals("end")) {
					break;
				}
			}
		}
		input.next();
	}

	private void continueToEnd(TokenStream input) {
		while (!input.lookAhead().toString().equals("end")) {
			input.next();
		}
	}

	private boolean conditionIsTrue() {
		return this.condition instanceof BooleanValue
				&& ((BooleanValue) condition).value == true;
	}

	private void getNextCondition(TokenStream input) throws Exception {		
		this.condition = new Expression(input).evaluate();
	}

	private void getStatementsForIf(TokenStream input) throws Exception {
		this.stmts = new ArrayList<Statement>();
		while (!input.lookAhead().toString().equals("end")
				&& !input.lookAhead().toString().equals("elif")
				&& !input.lookAhead().toString().equals("else")) {
			this.stmts.add(Statement.getStatement(input));
		}
	}

	@Override
	public void execute() throws Exception {
		for (Statement stmt : this.stmts) {
			stmt.execute();
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
