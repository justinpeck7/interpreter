package token;

import java.util.Arrays;

/**
 * Class that represents a token in the SILLY language.
 *   @author Dave Reed
 *   @version 2/8/16
 */
public class Token {
    private String strVal;

    public String[] keywords = { "=", "(", ")", "output", "quit", "repeat", "end", "if" };
    public String[] operators = { "+", "-", "*", "/", ".", "==", "=/=", ">", ">=", "<", "<=", "%", "and", "or", "not" };
    public String[] booleans = { "true", "false" };
    
    public static enum Type { KEYWORD, OPERATOR, IDENTIFIER, INTEGER, STRING, UNKNOWN, BOOLEAN }
    
    /**
     * Constructs a token out of the given string.
     *   @param str the string value of the token
     */
    public Token(String str) {
        this.strVal = str;
    }
    
    /**
     * Identifies what type of token it is.
     *   @return the token type (e.g., Token.Type.IDENTIFIER)
     */
    public Token.Type getType() {
        if (Arrays.asList(this.keywords).contains(this.strVal)) {
            return Token.Type.KEYWORD;
        }
        else if (Arrays.asList(this.operators).contains(this.strVal)) {
            return Token.Type.OPERATOR;
        }
        else if(Arrays.asList(this.booleans).contains(this.strVal)) {
        	return Token.Type.BOOLEAN;
        }
        else if (Character.isDigit(this.strVal.charAt(0)) || this.strVal.charAt(0) == '-') {
            for (int i = 1; i < this.strVal.length(); i++) {
                if (!Character.isDigit(this.strVal.charAt(i))) {
                    return Token.Type.UNKNOWN;
                }
            }
            return Token.Type.INTEGER;
        }
        else if (Character.isLetter(this.strVal.charAt(0))) {
            for (int i = 1; i < this.strVal.length(); i++) {
                if (!Character.isLetterOrDigit(this.strVal.charAt(i))) {
                    return Token.Type.UNKNOWN;
                }
            }
            return Token.Type.IDENTIFIER;
        }
        else if (this.strVal.charAt(0) == '"') {
            if (this.strVal.length() == 1 || this.strVal.charAt(this.strVal.length()-1) != '"') {
                return Token.Type.UNKNOWN;
            }
            return Token.Type.STRING;
        }
        else {
            return Token.Type.UNKNOWN;
        }
    }
    
    /**
     * Determines when two tokens are identical.
     *   @param other the other token being compared
     *   @return whether the two tokens represent the same string value
     */
    public boolean equals(Object other) {
        return this.strVal.equals(((Token)other).strVal);
    }
   
    /**
     * Converts the token to its string representation.
     *   @return the string representation
     */
    public String toString() {
        return this.strVal;
    }
    
    /**
     * Generates a hash code for a Token (based on its String hash code).
     *   @return a hash code for the Token
     */
    public int hashCode() {
    	return this.strVal.hashCode();
    }
}