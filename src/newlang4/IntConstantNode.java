package newlang4;

public class IntConstantNode extends Node {
	Node body;
	Value v;

	private IntConstantNode(LexicalUnit lu) {
		super.type = NodeType.INT_CONSTANT;
		v = lu.getValue();
	}

	public static Node isMatch(Environment env, LexicalUnit lu) {
		if (lu.getType() == LexicalType.INTVAL) {
			return new IntConstantNode(lu);
		}
		return null;
	}

	@Override
	public String toString() {
		return v.getSValue();
	}

	@Override
	public Value getValue() {
		return v;
	}
}
