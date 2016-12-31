package newlang4;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ExprListNode extends Node {
	private Node body;
	private List<Node> childNodeList = new ArrayList<Node>();
	private static Set<LexicalType> firstSet = new HashSet<LexicalType>();
	static {
		firstSet.add(LexicalType.SUB);
		firstSet.add(LexicalType.LP);
		firstSet.add(LexicalType.NAME);
		firstSet.add(LexicalType.INTVAL);
		firstSet.add(LexicalType.DOUBLEVAL);
		firstSet.add(LexicalType.LITERAL);
		firstSet.add(LexicalType.FUNC);
	};

	private ExprListNode(Environment env) {
		this.env = env;
		super.type = NodeType.EXPR_LIST;
	}

	public static Node isMatch(Environment env, LexicalUnit first) {
		if (firstSet.contains(first.getType())) {
			return new ExprListNode(env);
		}
		return null;
	}

	public boolean Parse() throws Exception {
		while (true) {
			LexicalUnit lu = env.getInput().get();
			env.getInput().unget(lu);

			body = ExprNode.isMatch(env, lu);
			if (body == null || !body.Parse()) {
				return false;
			}
			childNodeList.add(body);

			lu = env.getInput().get();
			if (lu.getType() != LexicalType.COMMA) {
				env.getInput().unget(lu);
				return true;
			}
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

	public List<Node> getList() {
		return childNodeList;
	}
}
