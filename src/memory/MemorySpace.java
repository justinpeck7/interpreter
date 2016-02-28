package memory;

import java.util.ArrayList;
import java.util.List;

import datavalue.DataValue;
import token.Token;

/**
 * Class that defines the memory space for the SILLY interpreter. 
 *   @author Dave Reed, edited by Justin Peck
 *   @version 2/8/16
 */
public class MemorySpace {
    private StackSegment stack;
    private HeapSegment heap;
    private List<StackSegment> scopes;
    private int scopeIndex;
    
    /**
     * Constructs an empty memory space.
     */
    public MemorySpace() {
    	this.heap = new HeapSegment();
    	this.scopes = new ArrayList<StackSegment>();
    	this.scopes.add(new StackSegment());
    	this.stack = this.scopes.get(0);
    	this.scopeIndex = 0;
    }
    
    /**
     * Creates a new scope.   
     */
    public void createNewScope() {
    	this.scopes.add(new StackSegment());
    	this.scopeIndex ++;
    	this.stack = this.scopes.get(this.scopeIndex);
    }
    
    /**
     * Destroys the current scope.   
     */
    public void destroyScope() throws Exception {
    	if (this.scopeIndex > 0) {
    		this.scopes.remove(scopeIndex);
        	this.scopeIndex --;
        	this.stack = this.scopes.get(this.scopeIndex);	
    	}
    	else {
    		throw new Exception("Cannot destroy the global scope");
    	}
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
     * Retrieves the value for a variable (from the current scope).
     * Searches all parent scopes until variable is found, else throws an Exception
     *   @param token the variable name
     *   @return the value stored under that name
     */
    public DataValue lookupVariable(Token token) throws Exception {
    	DataValue val = this.stack.lookup(token);
    	int searchIndex = this.scopeIndex -1;
    	while (val == null) {
    		if(searchIndex < 0) {
    			break;
    		}
    		else {
    			StackSegment currentStack = scopes.get(searchIndex);
    			val = currentStack.lookup(token);
    			searchIndex --;
    		}    		    		
    	}
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