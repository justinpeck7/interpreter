package token;

import java.util.Scanner;
import java.io.File;

/**
 * Class for reading SILLY language tokens from an input stream, either
 * standard input or from a file.
 *   @author Dave Reed
 *   @version 2/8/16
 */
public class TokenStream {
    private Scanner input;
    private Token nextToken;

    /**
     * Constructs a TokenStream connected to System.in.
     */
    public TokenStream() {
        input = new Scanner(System.in);
    }
    
    /**
     * Constructs a TokenStream connected to a file.
     *   @param filename the file to read from
     */
    public TokenStream(String filename) throws java.io.FileNotFoundException {
        input = new Scanner(new File(filename));
    }

    /**
     * Returns the next token in the TokenStream (without removing it).
     *   @return the next token
     */
    public Token lookAhead() {
        if (this.nextToken == null && input.hasNext()) {
            this.nextToken = new Token(input.next());
        }
        return this.nextToken;
    }
    
    /**
     * Returns the next token in the TokenStream (and removes it).
     *   @return the next token
     */
    public Token next() {
        if (this.nextToken == null && input.hasNext()) {
            this.nextToken = new Token(input.next());
        }
        
        Token safe = this.nextToken;
        this.nextToken = null;
        return safe;
     }
    
     /**
      * Returns the rest of the current line from the TokenStream.
      *   @return the line
      */
     public String nextLine() {
         if (this.nextToken != null) {
             String restOfLine = this.nextToken.toString() + " " + input.nextLine();
             this.nextToken = null;
             return restOfLine;
         }
         else {
             return input.nextLine();
         }
         
     }
     
     /**
      * Determines whether there are any more tokens to read.
      *   @return true if tokens remaining, else false
      */
     public boolean hasNext() {
        return (this.nextToken != null || input.hasNext());
     }
     
     /**
      * Determines whether there are any more lines to read in.
      *   @return true if line remains, else false
      */
     public boolean hasNextLine() {
        return (this.nextToken != null || input.hasNextLine());
     }
}