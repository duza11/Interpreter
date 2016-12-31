package newlang4;

public class CallFuncNode extends Node {
	private Node func;
	private Node exprList;

	private CallFuncNode(Environment env) {
		this.env = env;
		super.type = NodeType.FUNCTION_CALL;
	}

	public static Node isMatch(Environment env, LexicalUnit first) throws Exception {
		if (first.getType() != LexicalType.NAME) {
			return null;
		}
		LexicalUnit lu = env.getInput().get();
		lu = env.getInput().get();
		env.getInput().unget(first);
		env.getInput().unget(lu);
		if (lu.getType() != LexicalType.LP) {
			return null;
		}
		return new CallFuncNode(env);
	}

	public boolean Parse() throws Exception {
		LexicalUnit lu = env.getInput().get();
		func = VariableNode.isMatch(env, lu);
		if (func == null) {
			return false;
		}

		env.getInput().get();
		lu = env.getInput().get();
		env.getInput().unget(lu);
		exprList = ExprListNode.isMatch(env, lu);
		if (exprList == null || !exprList.Parse()) {
			return false;
		}

		lu = env.getInput().get();
		if (lu.getType() != LexicalType.RP) {
			return false;
		}

		return true;
	}

	public String toString() {
		return func.toString() + "[" + exprList.toString() + "]";
	}

	@Override
	public Value getValue() {
		return env.getFunction(func.toString()).invoke((ExprListNode) exprList);
	}
}
