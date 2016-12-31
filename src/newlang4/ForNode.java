package newlang4;

public class ForNode extends Node {
	private Node subst;
	private Node variable;
	private Node intCon;
	private Node stmtList;
	private LexicalUnit controlVariable;

	private ForNode(Environment env) {
		this.env = env;
		super.type = NodeType.FOR_STMT;
	}

	public static Node isMatch(Environment env, LexicalUnit first) {
		if (first.getType() == LexicalType.FOR) {
			return new ForNode(env);
		}
		return null;
	}

	@Override
	public boolean Parse() throws Exception {
		LexicalUnit lu = env.getInput().get();

		if (lu.getType() != LexicalType.FOR) {
			return false;
		}

		controlVariable = env.getInput().get();
		env.getInput().unget(controlVariable);
		subst = SubstNode.isMatch(env, controlVariable);
		if (subst == null || !subst.Parse()) {
			return false;
		}

		lu = env.getInput().get();
		if (lu.getType() != LexicalType.TO) {
			return false;
		}

		lu = env.getInput().get();
		intCon = IntConstantNode.isMatch(env, lu);
		if (lu == null) {
			return false;
		}

		lu = env.getInput().get();
		if (lu.getType() != LexicalType.NL) {
			return false;
		}

		lu = env.getInput().get();
		env.getInput().unget(lu);
		stmtList = StmtListNode.isMatch(env, lu);
		if (stmtList == null || !stmtList.Parse()) {
			return false;
		}

		lu = env.getInput().get();
		if (lu.getType() != LexicalType.NEXT) {
			return false;
		}

		lu = env.getInput().get();
		variable = VariableNode.isMatch(env, lu);
		if (variable == null || !controlVariable.getValue().getSValue().equals(lu.getValue().getSValue())) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return "For[[" + variable + " < " + intCon + "][" + stmtList + "]]";
	}

	@Override
	public Value getValue() {
		VariableNode v = env.getVariable(variable.toString());
		for (subst.getValue(); v.getValue().getIValue() <= intCon.getValue().getIValue();
				v.setValue(new ValueImpl(v.getValue().getIValue() + 1, ValueType.INTEGER))) {
			stmtList.getValue();
		}
		return super.getValue();
	}
}
