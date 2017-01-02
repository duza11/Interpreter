package newlang4;

public class ProgramNode extends Node {
	private Node stmtList;

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

		stmtList = StmtListNode.isMatch(env, lu);
		if (stmtList == null) {
			return false;
		}
		return stmtList.Parse();
	}

	@Override
	public String toString() {
		return stmtList.toString();
	}

	@Override
	public Value getValue() {
		return stmtList.getValue();
	}
}