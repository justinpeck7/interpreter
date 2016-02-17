package datavalue;

import token.Token;

/**
 * Interface that defines the data type operations for the SILLY language. 
 *   @author Dave Reed
 *   @version 2/8/16
 */
public interface DataValue extends Comparable<DataValue> {   
    public Object getValue();
    public Token.Type getType();
    public String toString();
}