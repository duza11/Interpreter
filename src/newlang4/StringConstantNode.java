package newlang4;

public class StringConstantNode extends Node {
	Node body;
	Value v;

	private StringConstantNode(LexicalUnit lu) {
		super.type = NodeType.STRING_CONSTANT;
		v = lu.getValue();
	}

	public static Node isMatch(Environment env, LexicalUnit lu) {
		if (lu.getType() == LexicalType.LITERAL) {
			return new StringConstantNode(lu);
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
