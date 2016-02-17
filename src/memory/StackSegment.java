package memory;

import java.util.Map;
import java.util.HashMap;

import datavalue.DataValue;
import token.Token;

/**
 * Class that models the stack segment of memory.
 *   @author Dave Reed
 *   @version 2/8/16
 */
public class StackSegment {
    private Map<Token, DataValue> varTable;
    
    /**
     * Constructs an empty collection of token/value pairs.
     */
    public StackSegment() {
    	this.varTable = new HashMap<Token, DataValue>();
    }
    
    /**
     * Stores a data value in the stack segment.
     *   @param variable the variable name the value is being stored under
     *   @param val the data value
     */
    public void store(Token variable, DataValue val) {
    	this.varTable.put(variable,  val); 	
    }
    
    /**
     * Looks up the value associated with a variable in the stack segment.
     *   @param variable the variable name being looked up
     *   @return the data value for that variable
     */
    public DataValue lookup(Token variable) {
    	return this.varTable.get(variable);
    }
}