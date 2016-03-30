package memory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import statement.Statement;
import token.Token;

/**
 * Class that represents a code segment in memory for subroutines
 * @author Justin Peck
 * @version 3/29/16
 */
public class CodeSegment {
	private Map<String, List<Statement>> subStatements;
	private Map<String, List<Token>> subParams;
	
	/**
	 * Constructs an empty CodeSegment
	 */
	public CodeSegment() {
		this.subStatements = new HashMap<String, List<Statement>>();
		this.subParams = new HashMap<String, List<Token>>();
	}
	
	/**
	 * Stores a given subroutine
	 * @param name the name of the subroutine to store
	 * @param statements the statements of the subroutine
	 * @param parameters the parameters of the subroutine
	 */
	public void storeSubroutine(Token name, List<Statement> statements, List<Token> parameters) {
		this.subStatements.put(name.toString(), statements);
		this.subParams.put(name.toString(), parameters);
	}
	
	/**
	 * Gets statements for a given subroutine
	 * @param name the name of the subroutine
	 * @return the statments of the subroutine
	 */
	public List<Statement> getStmtsForSub(Token name) {
		return this.subStatements.get(name.toString());
	}
	
	/**
	 * Gets parameters for a given subroutine
	 * @param name the name of the subroutine
	 * @return the parameters of the subroutine
	 */
	public List<Token> getParamsForSub(Token name) {
		return this.subParams.get(name.toString());
	}
}
