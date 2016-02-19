package statement;

import java.util.ArrayList;

import datavalue.BooleanValue;
import expression.Expression;
import token.Token;
import token.TokenStream;

/**
 * Derived class that represents an if statement in the SILLY language.
 *   @author Justin Peck
 *   @version 2/19/16
 */
public class If extends Statement {
	private ArrayList<Statement> stmts;
	private ArrayList<String> allLines;
	private Object condition;
	private boolean conditionMet = false;
	
    /**
     * Reads in an if statement from the specified stream
     *   @param input the stream to be read from
     */
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
					this.updateTotalLines(input);
					this.getNextCondition(input);
					if (conditionIsTrue()) {
						this.conditionMet = true;
						this.getStatementsForIf(input);
						this.continueToEnd(input);
					}
				} else if (input.lookAhead().toString().equals("else")) {
					this.updateTotalLines(input);
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

    /**
     * Adds current line to the array of all lines
     *   @param input the stream to read
     */
	private void updateTotalLines(TokenStream input) {
		this.allLines.add(input.next().toString());		
	}

	 /**
     * Continues to iterate through lines until 'end' is reached.
     * Adds each line to array of all lines
     *   @param input the stream to read
     */
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

	 /**
     * Checks if current condition evaluates to true
     */
	private boolean conditionIsTrue() {
		return this.condition instanceof BooleanValue
				&& ((BooleanValue) condition).value == true;
	}

	 /**
     * Gets condition following if/elif statements.
     * Adds condition to array of all lines
     *   @param input the stream to read
     */
	private void getNextCondition(TokenStream input) throws Exception {
		Expression check = new Expression(input);
		this.allLines.add(check.toString());
		this.condition = check.evaluate();
	}

	 /**
     * Gets all statements for the current if/elif/else block.
     * Add each statement to array of all lines
     *   @param input the stream to read
     */
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

	 /**
     * Executes the if statement
     */
	public void execute() throws Exception {
		for (Statement stmt : this.stmts) {
			stmt.execute();
		}
	}

    /**
     * Converts the current if statement into a String.
     *   @return the String representation of this statement
     */
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
