package newlang4;

public class ProgramNode extends Node {
	Environment env;
	Node stmt_list;

	private ProgramNode (Environment env) {
		this.env = env;
		super.type = NodeType.PROGRAM;
	}

	public static Node isMatch(Environment env, LexicalUnit first) {
		return new ProgramNode(env);
	}

	@Override
	public boolean Parse() throws Exception {
		LexicalUnit lu = env.getInput().get();
		env.getInput().unget(lu);

		stmt_list = StmtListNode.isMatch(env, lu);
		return stmt_list.Parse();
	}

	@Override
	public String toString() {
		return stmt_list.toString();
	}

	@Override
	public Value getValue() {
		return stmt_list.getValue();
	}
}