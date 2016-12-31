package newlang4;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class StmtListNode extends Node {
	Environment env;
	Node node;
	private List<Node> childNodeList = new ArrayList<Node>();
	private static Set<LexicalType> firstSet = new HashSet<LexicalType>();
	static {
		firstSet.add(LexicalType.NAME);
		firstSet.add(LexicalType.FOR);
		firstSet.add(LexicalType.END);
		firstSet.add(LexicalType.IF);
		firstSet.add(LexicalType.WHILE);
		firstSet.add(LexicalType.DO);
		firstSet.add(LexicalType.NL);

	};

	private StmtListNode(Environment env) {
		this.env = env;
		super.type = NodeType.STMT_LIST;
	}

	public static Node isMatch(Environment env, LexicalUnit first) {
		if (firstSet.contains(first.getType())) {
			return new StmtListNode(env);
		}
		return null;
	}

	@Override
	public boolean Parse() throws Exception {
		while (true) {
			LexicalUnit lu = env.getInput().get();
			if (lu.getType() == LexicalType.NL) {
				continue;
			}
			env.getInput().unget(lu);

			node = BlockNode.isMatch(env, lu);
			if (node != null) {
				if (node.Parse() == false) {
					return false;
				}
				childNodeList.add(node);
				continue;
			}

			node = StmtNode.isMatch(env, lu);
			if (node != null) {
				if (node.Parse() == false) {
					return false;
				}
				childNodeList.add(node);
				continue;
			}
			return true;
		}
	}

	@Override
	public String toString() {
		String tmp = "";
		Iterator<Node> itr = childNodeList.iterator();
		while (itr.hasNext()) {
			tmp += itr.next().toString();
			if (!itr.hasNext()) {
				break;
			}
			tmp += ";";
		}
		return tmp;
	}

	@Override
	public Value getValue() {
		for (Node node : childNodeList) {
			node.getValue();
		}
		return null;
	}
}