package datavalue;

import interpreter.Interpreter;
import token.Token;

/**
 * Class that represents a String value (which is stored in the heap).
 *   @author Dave Reed
 *   @version 2/8/16
 */
public class StringValue implements DataValue {
    private int address;

    /**
     * Constructs a String value.
     *   @param str the String being stored
     */
    public StringValue(String str) {
        this.address = Interpreter.MEMORY.storeDynamic(str);
    }

    /**
     * Accesses the stored String value.
     *   @return the String value (as an Object)
     */
    public Object getValue() {
        return Interpreter.MEMORY.lookupDynamic(this.address);
    }

    /**
     * Identifies the actual type of the value.
     *   @return Token.Type.STRING
     */
    public Token.Type getType() {
        return Token.Type.STRING;
    }

    /**
     * Converts the String value to a String.
     *   @return the stored String value
     */
    public String toString() {
        return (String) this.getValue();
    }

    /**
     * Comparison method for StringValues.
     *   @param other the value being compared with
     *   @return negative if <, 0 if ==, positive if >
     */
    public int compareTo(DataValue other) {
        return this.toString().compareTo(other.toString());
    }
}