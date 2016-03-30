package memory;

import java.util.ArrayList;
import java.util.List;

import datavalue.DataValue;
import statement.Statement;
import token.Token;

/**
 * Class that defines the memory space for the SILLY interpreter. 
 *   @author Dave Reed, edited by Justin Peck
 *   @version 3/29/16
 */
public class MemorySpace {
    private StackSegment stack;
    private HeapSegment heap;
    private CodeSegment code;
    private List<StackSegment> scopes;
    private int scopeIndex;
    
    /**
     * Constructs an empty memory space.
     */
    public MemorySpace() {
    	this.heap = new HeapSegment();
    	this.code = new CodeSegment();
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
    	int index = this.getStackIndexForVar(token);
    	if (index != -1) {
    		StackSegment currentStack = scopes.get(index);
    		currentStack.store(token, val);
    	}
    	else {
    		this.stack.store(token,  val);	
    	}    	
    }
    
    /**
     * Stores a value on the current scope    
     * @param token the variable name
     * @param val the value to be stored under that name
     */
    public void storeOnCurrentScope(Token token, DataValue val) {
    	this.stack.store(token, val);
    }
    
    public int getStackIndexForVar(Token token) {
    	int index = scopeIndex;
    	if(this.stack.lookup(token) != null) {
    		return index;
    	} else {
    		while (index > 1) {
    			index --;
        		StackSegment currentStack = this.scopes.get(index);
        		if(currentStack.lookup(token) != null) {
        			return index;
        		}
    		}    		
    	}
    	return -1;
    }
    
    /**
     * Stores a new variable/value in the stack segment.
     * If variable already exists, throws an exception.
     *   @param token the variable name
     *   @param val the value to be stored under that name
     */
    public void storeNewVariable(Token token, DataValue val) throws Exception {
    	if (this.stack.lookup(token) == null) {
    		this.stack.store(token,  val);
    	}
    	else {
    		throw new Exception("Variable " + token.toString() + " already declared");
    	}
    }

    /**
     * Retrieves the value for a variable (from the current scope).
     * Searches all parent scopes until variable is found, else throws an Exception
     *   @param token the variable name
     *   @return the value stored under that name
     */
    public DataValue lookupVariable(Token token) throws Exception {
    	DataValue val = this.stack.lookup(token);
			int searchIndex = this.scopeIndex - 1;
			while (val == null) {
				if (searchIndex < 0) {
					break;
				} else {
					StackSegment currentStack = scopes.get(searchIndex);
					val = currentStack.lookup(token);
					searchIndex--;
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
    
    
    /**
     * Stores a new subroutine in the code segment
     * @param name the name of the subroutine
     * @param statements the statements of the subroutine
     * @param parameters the parameter names of the subroutine
     */
    public void storeNewSubroutine(Token name, List<Statement> statements, List<Token> parameters) {
    	code.storeSubroutine(name, statements, parameters);
    }
    
    /**
     * Gets the statements for a given subroutine
     * @param name the name of the subroutine
     * @return the statements for the subroutine
     */
	public List<Statement> getStmtsForSub(Token name) {
		return this.code.getStmtsForSub(name);
	}
	
	/**
	 * Gets the params for a given subroutine
	 * @param name the name of the subroutine
	 * @return the params for the subroutine
	 */
	public List<Token> getParamsForSub(Token name) {
		return this.code.getParamsForSub(name);
	}
}