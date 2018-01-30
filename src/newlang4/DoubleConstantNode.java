package newlang4;

public class DoubleConstantNode extends Node {
	private Value v;

	private DoubleConstantNode(LexicalUnit lu) {
		super.type = NodeType.DOUBLE_CONSTANT;
		v = lu.getValue();
	}

	public static Node isMatch(Environment env, LexicalUnit lu) {
		if (lu.getType() == LexicalType.DOUBLEVAL) {
			return new DoubleConstantNode(lu);
		}
		return null;
	}

	@Override
	public ValueType getType() {
		return v.getType();
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
