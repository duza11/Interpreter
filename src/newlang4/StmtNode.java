package newlang4;

import java.util.HashSet;
import java.util.Set;

public class StmtNode extends Node {
	Environment env;
	Node body;
	private static Set<LexicalType> firstSet = new HashSet<LexicalType>();
	static {
		firstSet.add(LexicalType.NAME);
		firstSet.add(LexicalType.FOR);
		firstSet.add(LexicalType.END);
	};

	private StmtNode(Environment env) {
		this.env = env;
		super.type = NodeType.STMT;
	}

	public static Node isMatch(Environment env, LexicalUnit first) {
		if (firstSet.contains(first.getType())) {
			return new StmtNode(env);
		}
		return null;
	}

	@Override
	public boolean Parse() throws Exception {
		LexicalUnit lu = env.getInput().get();

		if (lu.getType() == LexicalType.END) {
			body = new Node(NodeType.END);
			return true;
		}

		env.getInput().unget(lu);

		body = SubstNode.isMatch(env, lu);
		if (body != null) {
			return body.Parse();
		}

		body = CallSubNode.isMatch(env, lu);
		if (body != null) {
			return body.Parse();
		}

		body = ForNode.isMatch(env, lu);
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