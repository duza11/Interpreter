package newlang4;

import java.util.HashSet;
import java.util.Set;

public class BlockNode extends Node {
	private Node body;
	private static Set<LexicalType> firstSet = new HashSet<LexicalType>();
	static {
		firstSet.add(LexicalType.IF);
		firstSet.add(LexicalType.WHILE);
		firstSet.add(LexicalType.DO);
	};

	private BlockNode(Environment env) {
		this.env = env;
		super.type = NodeType.BLOCK;
	}

	public static Node isMatch(Environment env, LexicalUnit lex) {
		if (firstSet.contains(lex.getType())) {
			return new BlockNode(env);
		}
		return null;
	}

	@Override
	public boolean Parse() throws Exception {
		LexicalUnit lu = env.getInput().get();
		env.getInput().unget(lu);

		body = IfNode.isMatch(env, lu);
		if (body != null) {
			return body.Parse();
		}

		body = LoopNode.isMatch(env, lu);
		if (body != null) {
			return body.Parse();
		}

		return false;
	}

	@Override
	public String toString() {
		return body.toString();
	}

	@Override
	public Value getValue() {
		return body.getValue();
	}
}