package newlang4;

import java.util.HashSet;
import java.util.Set;

public class UnaryNode extends Node {
	private Node body;
	private static Set<LexicalType> firstSet = new HashSet<LexicalType>();
	static {
		firstSet.add(LexicalType.SUB);
		firstSet.add(LexicalType.LP);
		firstSet.add(LexicalType.NAME);
		firstSet.add(LexicalType.INTVAL);
		firstSet.add(LexicalType.DOUBLEVAL);
		firstSet.add(LexicalType.LITERAL);
	}

	private UnaryNode(Environment env) {
		this.env = env;
	}

	public static Node isMatch(Environment env, LexicalUnit first) {
		if (firstSet.contains(first.getType())) {
			return new UnaryNode(env);
		}
		return null;
	}

	@Override
	public boolean Parse() throws Exception {
		LexicalUnit lu = env.getInput().get();
		if (lu.getType() == LexicalType.LP) {
			lu = env.getInput().get();
			env.getInput().unget(lu);
			body = ExprNode.isMatch(env, lu);
			if (body == null || !body.Parse()) {
				return false;
			}

			lu = env.getInput().get();
			if (lu.getType() == LexicalType.RP) {
				return true;
			}
			return false;
		}

		if (lu.getType() == LexicalType.SUB) {
			lu = env.getInput().get();
			env.getInput().unget(lu);
			body = UnaryNode.isMatch(env, lu);
			if (body != null && body.Parse()) {
				return true;
			}
		}

		body = IntConstantNode.isMatch(env, lu);
		if (body != null) {
			return true;
		}

		body = DoubleConstantNode.isMatch(env, lu);
		if (body != null) {
			return true;
		}

		body = StringConstantNode.isMatch(env, lu);
		if (body != null) {
			return true;
		}

		env.getInput().unget(lu);
		body = CallFuncNode.isMatch(env, lu);
		if (body != null && body.Parse()) {
			return true;
		}

		lu = env.getInput().get();
		body = VariableNode.isMatch(env, lu);
		if (body != null) {
			return true;
		}

		return false;
	}

	@Override
	public ValueType getType() {
		return body.getValue().getType();
	}

	@Override
	public String toString() {
		return "-[" + body + "]";
	}

	@Override
	public Value getValue() {
		Value v = body.getValue();
		switch (v.getType()) {
		case DOUBLE:
			return new ValueImpl(-v.getDValue(), ValueType.DOUBLE);
		default:
			return new ValueImpl(-v.getIValue(), ValueType.INTEGER);
		}
	}
}