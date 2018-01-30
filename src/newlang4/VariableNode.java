package newlang4;

public class VariableNode extends Node {
	String var_name;
	Value v;

	/** Creates a new instance of variable */
	public VariableNode(String name) {
		var_name = name;
	}

	public VariableNode(LexicalUnit u) {
		var_name = u.getValue().getSValue();
	}

	public static Node isMatch(Environment my_env, LexicalUnit first) {
		if (first.getType() == LexicalType.NAME) {
			VariableNode v;

			String s = first.getValue().getSValue();
			v = my_env.getVariable(s);
			return v;

			// return new VariableNode(first.getValue().getSValue());
		}
		return null;
	}

	public void setValue(Value my_v) {
		v = my_v;
	}

	public Value getValue() {
		return v;
	}

	@Override
	public ValueType getType() {
		return v.getType();
	}

	@Override
	public String toString() {
		return var_name;
	}
}
