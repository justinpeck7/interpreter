package statement;

import java.util.ArrayList;

import datavalue.BooleanValue;
import datavalue.DataValue;
import expression.Expression;
import token.Token;
import token.TokenStream;

public class While extends Statement {
	private Expression condition;
	private ArrayList<Statement> stmts;

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

	public void execute() throws Exception {
		DataValue flag = this.condition.evaluate();
		while (flag instanceof BooleanValue
				&& ((BooleanValue) flag).value == true) {
			for (Statement stmt : stmts) {
				stmt.execute();
			}
			flag = this.condition.evaluate();
		}
	}

	public String toString() {
        String str = "while " + this.condition + "\n";
        for (Statement stmt : this.stmts) {
            str += "    " + stmt + "\n";
        }
        return str + "end";
    }

}
