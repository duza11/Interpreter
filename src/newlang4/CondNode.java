package newlang4;

import java.util.HashSet;
import java.util.Set;

public class CondNode extends Node {
	private Node left;
	private Node right;
	private static Set<LexicalType> firstSet = new HashSet<LexicalType>();
	LexicalType operator;
	static {
		firstSet.add(LexicalType.SUB);
		firstSet.add(LexicalType.LP);
		firstSet.add(LexicalType.NAME);
		firstSet.add(LexicalType.INTVAL);
		firstSet.add(LexicalType.DOUBLEVAL);
		firstSet.add(LexicalType.LITERAL);
	};

	private CondNode(Environment env) {
		this.env = env;
		super.type = NodeType.COND;
	}

	public static Node isMatch(Environment env, LexicalUnit first) {
		if (firstSet.contains(first.getType())) {
			return new CondNode(env);
		}
		return null;
	}

	@Override
	public boolean Parse() throws Exception {
		LexicalUnit lu = env.getInput().get();
		env.getInput().unget(lu);
		left = ExprNode.isMatch(env, lu);
		if (left == null || !left.Parse()) {
			return false;
		}

		lu = env.getInput().get();
		operator = lu.getType();
		if (operator != LexicalType.EQ && operator != LexicalType.GT && operator != LexicalType.LT && operator != LexicalType.GE && operator != LexicalType.LE && operator != LexicalType.NE) {
			return false;
		}

		lu = env.getInput().get();
		env.getInput().unget(lu);
		right = ExprNode.isMatch(env, lu);
		if (right == null || !right.Parse()) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		switch (operator) {
		case EQ:
			return "=" + "[" + right + " : " + left + "]";
		case GT:
			return ">" + "[" + right + " : " + left + "]";
		case LT:
			return "<" + "[" + right + " : " + left + "]";
		case GE:
			return ">=" + "[" + right + " : " + left + "]";
		case LE:
			return "<=" + "[" + right + " : " + left + "]";
		case NE:
			return "<>" + "[" + right + " : " + left + "]";
		default:
			return null;
		}
	}

	@Override
	public Value getValue() {
		switch (operator) {
		case EQ:
			if (left.getValue().getType() == ValueType.STRING || right.getValue().getType() == ValueType.STRING) {
				return new ValueImpl(left.getValue().getSValue().equals(right.getValue().getSValue()), ValueType.BOOL);
			} else if (left.getValue().getType() == ValueType.DOUBLE || right.getValue().getType() == ValueType.DOUBLE) {
				return new ValueImpl(left.getValue().getDValue() == right.getValue().getDValue(), ValueType.BOOL);
			}
			return new ValueImpl(left.getValue().getIValue() == right.getValue().getIValue(), ValueType.BOOL);
		case GT:
			if (left.getValue().getType() == ValueType.DOUBLE || right.getValue().getType() == ValueType.DOUBLE) {
				return new ValueImpl(left.getValue().getDValue() > right.getValue().getDValue(), ValueType.BOOL);
			}
			return new ValueImpl(left.getValue().getIValue() > right.getValue().getIValue(), ValueType.BOOL);
		case LT:
			if (left.getValue().getType() == ValueType.DOUBLE || right.getValue().getType() == ValueType.DOUBLE) {
				return new ValueImpl(left.getValue().getDValue() < right.getValue().getDValue(), ValueType.BOOL);
			}
			return new ValueImpl(left.getValue().getIValue() < right.getValue().getIValue(), ValueType.BOOL);
		case GE:
			if (left.getValue().getType() == ValueType.DOUBLE || right.getValue().getType() == ValueType.DOUBLE) {
				return new ValueImpl(left.getValue().getDValue() >= right.getValue().getDValue(), ValueType.BOOL);
			}
			return new ValueImpl(left.getValue().getIValue() >= right.getValue().getIValue(), ValueType.BOOL);
		case LE:
			if (left.getValue().getType() == ValueType.DOUBLE || right.getValue().getType() == ValueType.DOUBLE) {
				return new ValueImpl(left.getValue().getDValue() <= right.getValue().getDValue(), ValueType.BOOL);
			}
			return new ValueImpl(left.getValue().getIValue() <= right.getValue().getIValue(), ValueType.BOOL);
		case NE:
			if (left.getValue().getType() == ValueType.STRING || right.getValue().getType() == ValueType.STRING) {
				return new ValueImpl(!left.getValue().getSValue().equals(right.getValue().getSValue()), ValueType.BOOL);
			} else if (left.getValue().getType() == ValueType.DOUBLE || right.getValue().getType() == ValueType.DOUBLE) {
				return new ValueImpl(left.getValue().getDValue() != right.getValue().getDValue(), ValueType.BOOL);
			}
			return new ValueImpl(left.getValue().getIValue() != right.getValue().getIValue(), ValueType.BOOL);
		}
		return new ValueImpl(false, ValueType.BOOL);
	}
}