package statement;

import java.util.ArrayList;

import datavalue.BooleanValue;
import expression.Expression;
import token.Token;
import token.TokenStream;

public class If extends Statement {
	private ArrayList<Statement> stmts;
	private ArrayList<String> allLines;
	private Object condition;
	private boolean conditionMet = false;

	public If(TokenStream input) throws Exception {
		Token keyword = input.next();
		this.allLines = new ArrayList<String>();
		this.stmts = new ArrayList<Statement>();

		if (!keyword.toString().equals("if")) {
			throw new Exception("SYNTAX ERROR: Malformed if statement");
		}
		
		this.getNextCondition(input);

		if (conditionIsTrue()) {
			this.conditionMet = true;
			this.getStatementsForIf(input);
			this.continueToEnd(input);
		} else {
			while (!this.conditionMet) {
				while (!input.lookAhead().toString().equals("elif")
						&& !input.lookAhead().toString().equals("else")
						&& !input.lookAhead().toString().equals("end")) {
					Statement stmt = Statement.getStatement(input);
					this.allLines.add("" + stmt);
				}

				if (input.lookAhead().toString().equals("elif")) {
					this.updateAllLines(input);
					input.next();
					this.getNextCondition(input);
					if (conditionIsTrue()) {
						this.conditionMet = true;
						this.getStatementsForIf(input);
						this.continueToEnd(input);
					}
				} else if (input.lookAhead().toString().equals("else")) {
					this.updateAllLines(input);
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

	private void updateAllLines(TokenStream input) {
		this.allLines.add(input.lookAhead().toString());		
	}

	private void continueToEnd(TokenStream input) throws Exception {
		while (!input.lookAhead().toString().equals("end")) {
			if(input.lookAhead().toString().equals("elif")) {
				this.allLines.add(input.nextLine());
			}
			else if (input.lookAhead().toString().equals("else")) {
				this.allLines.add(input.nextLine());
			}
			else {
				Statement stmt = Statement.getStatement(input);
				this.allLines.add(stmt + "");	
			}
		}
	}

	private boolean conditionIsTrue() {
		return this.condition instanceof BooleanValue
				&& ((BooleanValue) condition).value == true;
	}

	private void getNextCondition(TokenStream input) throws Exception {
		Expression check = new Expression(input);
		this.allLines.add(check.toString());
		this.condition = check.evaluate();
	}

	private void getStatementsForIf(TokenStream input) throws Exception {
		this.stmts = new ArrayList<Statement>();
		while (!input.lookAhead().toString().equals("end")
				&& !input.lookAhead().toString().equals("elif")
				&& !input.lookAhead().toString().equals("else")) {
			Statement stmt = Statement.getStatement(input);
			this.stmts.add(stmt);
			this.allLines.add(stmt + "");
		}
	}

	public void execute() throws Exception {
		for (Statement stmt : this.stmts) {
			stmt.execute();
		}
	}

	public String toString() {
		String str = "if ";
		for (String line : allLines) {
			if(!line.equals("elif")) {
				str += line + "\n";	
			}
			else {
				str += line;
			}
		}
		str += "end";	
		return str;
	}

}
