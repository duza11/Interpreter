package newlang4;

public class CallSubNode extends Node {
	private Node sub;
	private Node exprList;

	private CallSubNode(Environment env) {
		this.env = env;
		super.type = NodeType.SUB_CALL;
	}

	public static Node isMatch(Environment env, LexicalUnit first) {
		if (first.getType() == LexicalType.NAME) {
			return new CallSubNode(env);
		}
		return null;
	}

	public boolean Parse() throws Exception {
		LexicalUnit lu = env.getInput().get();
		sub = VariableNode.isMatch(env, lu);
		if (sub == null) {
			return false;
		}

		lu = env.getInput().get();
		env.getInput().unget(lu);
		exprList = ExprListNode.isMatch(env, lu);
		if (exprList != null && exprList.Parse()) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return sub.toString() + "[" + exprList.toString() + "]";
	}

	@Override
	public Value getValue() {
		return env.getFunction(sub.toString()).invoke((ExprListNode) exprList);
	}
}
