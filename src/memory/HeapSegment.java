package memory;

import java.util.LinkedList;

/**
 * Class that models the heap segment of memory.
 *   @author Dave Reed
 *   @version 2/8/16
 */
public class HeapSegment {
    private LinkedList<Object> dynamicList;
    
    /**
     * Constructs an empty memory heap.
     */
    public HeapSegment() {
    	this.dynamicList = new LinkedList<Object>();
    }
    
    /**
     * Stores a value in the heap.
     *   @param dynValue the value being stored
     *   @return the address (index) where the value is stored in the heap
     */
    public int store(Object dynValue) {
    	this.dynamicList.add(dynValue);
    	return this.dynamicList.size()-1;
    }
    
    /**
     * Removes the values stored at the specified address.
     *   @param index the address (index) of the desired value
     *   @return the value stored at that index
     *   @throws IndexOutOfBoundsException 
     */
    public Object remove(int index) throws IndexOutOfBoundsException {
    	if (index < 0 || index >= this.dynamicList.size()) {
    		throw new IndexOutOfBoundsException("Illegal HEAP access");
    	}
    	Object obj = this.dynamicList.get(index);    
        this.dynamicList.remove(index);
        return obj;
    }
    
    /**
     * Looks up a value stored in the heap.
     *   @param index the address (index) of the desired value
     *   @return the value stored at that index
     *   @throws IndexOutOfBoundsException 
     */
    public Object lookup(int index) throws IndexOutOfBoundsException {
    	if (index < 0 || index >= this.dynamicList.size()) {
    		throw new IndexOutOfBoundsException("Illegal HEAP access");
    	}
    	return this.dynamicList.get(index);
    }
}