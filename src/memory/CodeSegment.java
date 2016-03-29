package memory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import statement.Statement;
import token.Token;

public class CodeSegment {
	private Map<String, List<Statement>> subStatements;
	private Map<String, List<Token>> subParams;
	
	public CodeSegment() {
		this.subStatements = new HashMap<String, List<Statement>>();
		this.subParams = new HashMap<String, List<Token>>();
	}
	
	public void storeSubroutine(Token name, List<Statement> statements, List<Token> parameters) {
		this.subStatements.put(name.toString(), statements);
		this.subParams.put(name.toString(), parameters);
	}
	
	public List<Statement> getStmtsForSub(Token name) {
		return this.subStatements.get(name.toString());
	}
	
	public List<Token> getParamsForSub(Token name) {
		return this.subParams.get(name.toString());
	}
}
