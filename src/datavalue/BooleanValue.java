package datavalue;

import token.Token;

/**
* Class that represents a boolean value.
*   @author Justin Peck
*   @version 2/17/16
*/
public class BooleanValue implements DataValue {
	
	public boolean value;
	
	public BooleanValue(boolean bool) {
		this.value = bool;		
	}
	
	 /**
     * Accesses the stored boolean value.
     *   @return the boolean value (as an Object)
     */
    public Object getValue() {
        return new Boolean(this.value);
    }
    
    /**
     * Identifies the actual type of the value.
     *   @return Token.Type.BOOLEAN
     */
    public Token.Type getType() {
        return Token.Type.BOOLEAN;
    }
    
    /**
     * Converts the boolean value to a String.
     *   @return a String representation of the boolean value
     */
    public String toString() {
        return "" + this.value;
    }

    /**
     * Comparison method for BooleanValues.
     *   @param other value being compared with
     *   @return 1 if equal, 0 if not
     */
    public int compareTo(DataValue other) {
    	if(this.value == ((BooleanValue) other).value) {
    		return 1;
    	}
    	else {
    		return -1;
    	}
    }
}
