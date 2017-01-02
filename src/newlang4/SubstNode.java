package newlang4;

public class SubstNode extends Node {
	private Node var;
	private Node right;

	private SubstNode(Environment env) {
		this.env = env;
		super.type = NodeType.ASSIGN_STMT;
	}

	public static Node isMatch(Environment env, LexicalUnit first) {
		try {
			if (first.getType() != LexicalType.NAME) {
				return null;
			}
			LexicalUnit lu = env.getInput().get();
			lu = env.getInput().get();
			env.getInput().unget(first);
			env.getInput().unget(lu);

			if (lu.getType() != LexicalType.EQ) {
				return null;
			}
			return new SubstNode(env);
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean Parse() throws Exception {
		LexicalUnit lu = env.getInput().get();
		var = VariableNode.isMatch(env, lu);

		lu = env.getInput().get();
		lu = env.getInput().get();
		env.getInput().unget(lu);

		right = ExprNode.isMatch(env, lu);
		if (right == null) {
			return false;
		}
		return right.Parse();
	}

	@Override
	public String toString() {
		return var.toString()+ "[" + right.toString() + "]";
	}

	@Override
	public Value getValue() {
		env.getVariable(var.toString()).setValue(right.getValue());
		return super.getValue();
	}
}
