package expression;

import interpreter.Interpreter;
import datavalue.BooleanValue;
import datavalue.DataValue;
import datavalue.IntegerValue;
import datavalue.StringValue;
import token.Token;
import token.TokenStream;

/**
 * Derived class that represents an expression in the SILLY language.
 *   @author Dave Reed
 * @version 2/8/16
 */
public class Expression {
	private Expression lhs;
	private Token middle;
	private Expression rhs;

	/**
	 * Creates an expression from the specified TokenStream.
	 * 
	 * @param input
	 *            the TokenStream from which the program is read
	 */
	public Expression(TokenStream input) throws Exception {
		Token token1 = input.next();
		if (token1.toString().equals("(")) {
			if (input.lookAhead().toString().equals("-") ||
					input.lookAhead().toString().equals("not")) {
				this.lhs = null;
			} else {
				this.lhs = new Expression(input);
			}
			this.middle = input.next();
			this.rhs = new Expression(input);
			input.next();

			if (this.middle.getType() != Token.Type.OPERATOR) {
				throw new Exception("SYNTAX ERROR: Malformed expression");
			}
		} else {
			this.lhs = null;
			this.middle = token1;
			this.rhs = null;

			if (this.middle.getType() != Token.Type.INTEGER
					&& this.middle.getType() != Token.Type.STRING
					&& this.middle.getType() != Token.Type.IDENTIFIER
					&& this.middle.getType() != Token.Type.BOOLEAN) {
				throw new Exception("SYNTAX ERROR: Malformed expression");
			}
		}
	}

	/**
	 * Private helper method, called by evaluate.
	 */
	private DataValue getValue(Token t) throws Exception {
		if (t.getType() == Token.Type.IDENTIFIER) {
			return Interpreter.MEMORY.lookupVariable(t);
		} else if (t.getType() == Token.Type.INTEGER) {
			return new IntegerValue(Integer.parseInt(t.toString()));
		} else if (t.getType() == Token.Type.STRING) {
			return new StringValue(t.toString());
		} else if (t.getType() == Token.Type.BOOLEAN) {
			return new BooleanValue(Boolean.parseBoolean(t.toString()));
		} else {
			throw new Exception("RUNTIME ERROR: Invalid data type");
		}
	}

	/**
	 * Evaluates the current expression involving integers and/or variables.
	 * 
	 * @param bindings
	 *            the current variable and subroutine bindings
	 * @return the value represented by the expression
	 */
	public DataValue evaluate() throws Exception {
		if (this.lhs == null & this.rhs == null) {
			return this.getValue(this.middle);
		} else {
			DataValue lhsValue = new IntegerValue(0);
			if (this.lhs != null) {
				lhsValue = this.lhs.evaluate();
			}
			DataValue rhsValue = this.rhs.evaluate();

			if (this.middle.toString().equals(".")) {
				String s1 = lhsValue.getValue().toString();
				String s2 = rhsValue.getValue().toString();
				if (s1.length() == 0 || s1.charAt(0) != '"') {
					s1 = '"' + s1 + '"';
				}
				if (s2.length() == 0 || s2.charAt(0) != '"') {
					s2 = '"' + s2 + '"';
				}
				return new StringValue(s1.substring(0, s1.length() - 1)
						+ s2.substring(1, s2.length()));
			} else if (lhsValue.getType() == Token.Type.INTEGER
					&& rhsValue.getType() == Token.Type.INTEGER) {
				int i1 = ((Integer) (lhsValue.getValue())).intValue();
				int i2 = ((Integer) (rhsValue.getValue())).intValue();

				if (this.middle.toString().equals("+")) {
					return new IntegerValue(i1 + i2);
				} else if (this.middle.toString().equals("-")) {
					return new IntegerValue(i1 - i2);
				} else if (this.middle.toString().equals("*")) {
					return new IntegerValue(i1 * i2);
				} else if (this.middle.toString().equals("/")) {
					return new IntegerValue(i1 / i2);
				} else if (this.middle.toString().equals("%")) {
					return new IntegerValue(i1 % i2);
				} else if (this.middle.toString().equals("==")) {
					return new BooleanValue(i1 == i2);
				} else if (this.middle.toString().equals("=/=")) {
					return new BooleanValue(i1 != i2);
				} else if (this.middle.toString().equals(">")) {
					return new BooleanValue(i1 > i2);
				} else if (this.middle.toString().equals("<")) {
					return new BooleanValue(i1 < i2);
				} else if (this.middle.toString().equals(">=")) {
					return new BooleanValue(i1 >= i2);
				} else if (this.middle.toString().equals("<=")) {
					return new BooleanValue(i1 <= i2);
				}
			} else if (lhsValue.getType() == Token.Type.STRING
					&& rhsValue.getType() == Token.Type.STRING
					&& !this.middle.toString().equals(".")) {

				String s1 = (String) lhsValue.getValue();
				String s2 = (String) rhsValue.getValue();

				if (this.middle.toString().equals("==")) {
					return new BooleanValue(s1.equals(s2));
				}
				if (this.middle.toString().equals("=/=")) {
					return new BooleanValue(!s1.equals(s2));
				}
			} else if (lhsValue.getType() == Token.Type.BOOLEAN
					&& rhsValue.getType() == Token.Type.BOOLEAN) {
				boolean b1 = (boolean) lhsValue.getValue();
				boolean b2 = (boolean) rhsValue.getValue();

				if (this.middle.toString().equals("and")) {
					return new BooleanValue(b1 && b2);
				} else if (this.middle.toString().equals("or")) {
					return new BooleanValue(b1 || b2);
				}
			}
			else if (this.lhs == null && rhsValue.getType() == Token.Type.BOOLEAN) {
				boolean b = (boolean) rhsValue.getValue();
				if(this.middle.toString().equals("not")) {
					return new BooleanValue(!b);
				}
			}
			else {
				throw new Exception("RUNTIME ERROR: illegal operands for "
						+ this.middle);
			}
		}
		return null;
	}

	/**
	 * Converts the current expression into a String.
	 * 
	 * @return the String representation of this expression
	 */
	public String toString() {
		if (this.lhs == null) {
			if (this.rhs == null) {
				return this.middle.toString();
			} else {
				return "( " + this.middle + " " + this.rhs + " )";
			}
		} else {
			return "( " + this.lhs + " " + this.middle + " " + this.rhs + " )";
		}
	}
}