package statement;

import interpreter.Interpreter;

import java.util.ArrayList;

import datavalue.BooleanValue;
import datavalue.DataValue;
import expression.Expression;
import token.Token;
import token.TokenStream;

/**
 * Derived class that represents a while statement in the SILLY language.
 *   @author Justin Peck
 *   @version 2/19/16
 */
public class While extends Statement {
	private Expression condition;
	private ArrayList<Statement> stmts;

    /**
     * Reads in a while statement from the specified stream
     *   @param input the stream to be read from
     */
	public While(TokenStream input) throws Exception {
		Token keyword = input.next();
		if (!keyword.toString().equals("while")) {
			throw new Exception("SYNTAX ERROR: Malformed while statement");
		}

		this.condition = new Expression(input);
		this.stmts = new ArrayList<Statement>();

		while (!input.lookAhead().toString().equals("end")) {
			this.stmts.add(Statement.getStatement(input));
		}
		input.next();
	}

    /**
     * Executes the current while statement
     */
	public void execute() throws Exception {
		Interpreter.MEMORY.createNewScope();
		DataValue flag = this.condition.evaluate();
		if(!(flag instanceof BooleanValue)) {
			throw new Exception("While expression must be a boolean");
		}
		else {
			while (((BooleanValue) flag).value == true) {
				for (Statement stmt : stmts) {
					stmt.execute();
				}
				flag = this.condition.evaluate();
			}
		}
		Interpreter.MEMORY.destroyScope();
	}

    /**
     * Converts the current while statement into a String.
     *   @return the String representation of this statement
     */
	public String toString() {
        String str = "while " + this.condition + "\n";
        for (Statement stmt : this.stmts) {
            str += "    " + stmt + "\n";
        }
        return str + "end";
    }

}
