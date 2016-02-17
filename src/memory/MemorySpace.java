package memory;

import datavalue.DataValue;
import token.Token;

/**
 * Class that defines the memory space for the SILLY interpreter. 
 *   @author Dave Reed 
 *   @version 2/8/16
 */
public class MemorySpace {
    private StackSegment stack;
    private HeapSegment heap;
    
    /**
     * Constructs an empty memory space.
     */
    public MemorySpace() {
    	this.stack = new StackSegment();
    	this.heap = new HeapSegment();
    }
    
    /**
     * Stores a variable/value in the stack segment.
     *   @param token the variable name
     *   @param val the value to be stored under that name
     */
    public void storeVariable(Token token, DataValue val) {
    	this.stack.store(token,  val);
    }

    /**
     * Retrieves the value for a variable (from the stack segment).
     *   @param token the variable name
     *   @return the value stored under that name
     */
    public DataValue lookupVariable(Token token) throws Exception {
    	DataValue val = this.stack.lookup(token);
        if (val == null) {
            throw new Exception("Undefined variable: " + token.toString());
        }
        return val;
    }

    /**
     * Stores a string constant in the heap segment.
     *   @param str the string constant
     *   @return the heap index where that string constant is stored
     */
    public int storeDynamic(Object obj) {
    	return this.heap.store(obj);
    }

    /**
     * Retrieves a string constant from the heap segment.
     * @param n the heap index of the string constant
     * @return the string constant at index n
     */
    public Object lookupDynamic(int n) {
    	return this.heap.lookup(n);
    }
}