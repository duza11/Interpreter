package newlang4;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class IfNode extends Node {
	private Node cond;
	private Node stmt;
	private static Set<LexicalType> firstSet = new HashSet<LexicalType>();
	static {
		firstSet.add(LexicalType.IF);
	};
	private Map<Node, Node> nodeMap = new LinkedHashMap<>();

	private IfNode(Environment env) {
		this.env = env;
		super.type = NodeType.IF_BLOCK;
	}

	public static Node isMatch(Environment env, LexicalUnit first) {
		if (firstSet.contains(first.getType())) {
			return new IfNode(env);
		}
		return null;
	}

	@Override
	public boolean Parse() throws Exception {
		LexicalUnit lu = env.getInput().get();
		if (!isIfPrefix(lu)) {
			return false;
		}

		lu = env.getInput().get();
		env.getInput().unget(lu);
		stmt = StmtNode.isMatch(env, lu);
		if (stmt != null) {
			if (!stmt.Parse()) {
				return false;
			}
			nodeMap.put(cond, stmt);

			lu = env.getInput().get();
			if (lu.getType() == LexicalType.ELSE) {
				lu = env.getInput().get();
				env.getInput().unget(lu);
				stmt = StmtNode.isMatch(env, lu);
				if (stmt == null || !stmt.Parse()) {
					return false;
				}
				nodeMap.put(null, stmt);
			} else {
				env.getInput().unget(lu);
			}
		} else if (lu.getType() == LexicalType.NL) {
			env.getInput().get();
			lu = env.getInput().get();
			env.getInput().unget(lu);
			stmt = StmtListNode.isMatch(env, lu);
			if (stmt == null || !stmt.Parse()) {
				return false;
			}
			nodeMap.put(cond, stmt);

			lu = env.getInput().get();
			if (!isElseBlock(lu)) {
				return false;
			}

			lu = env.getInput().get();
			if (lu.getType() != LexicalType.ENDIF) {
				return false;
			}

		} else {
			return false;
		}

		lu = env.getInput().get();
		if (lu.getType() != LexicalType.NL) {
			return false;
		}

		return true;
	}

	private boolean isIfPrefix(LexicalUnit lu) throws Exception {
		if (lu.getType() != LexicalType.IF) {
			return false;
		}

		lu = env.getInput().get();
		env.getInput().unget(lu);
		cond = CondNode.isMatch(env, lu);
		if (cond == null || !cond.Parse()) {
			return false;
		}

		lu = env.getInput().get();
		if (lu.getType() != LexicalType.THEN) {
			return false;
		}

		return true;
	}

	private boolean isElseBlock(LexicalUnit lu) throws Exception {
		if (!isElseIfBlock(lu)) {
			return false;
		}

		lu = env.getInput().get();
		if (lu.getType() == LexicalType.ELSE) {
			lu = env.getInput().get();
			if (lu.getType() != LexicalType.NL) {
				return false;
			}

			lu = env.getInput().get();
			env.getInput().unget(lu);
			stmt = StmtListNode.isMatch(env, lu);
			if (stmt == null || !stmt.Parse()) {
				return false;
			}
			nodeMap.put(null, stmt);
		} else {
			env.getInput().unget(lu);
		}

		return true;
	}

	private boolean isElseIfBlock(LexicalUnit lu) throws Exception {
		while (lu.getType() == LexicalType.ELSEIF) {
			lu = env.getInput().get();
			env.getInput().unget(lu);
			cond = CondNode.isMatch(env, lu);
			if (cond == null || !cond.Parse()) {
				return false;
			}

			lu = env.getInput().get();
			if (lu.getType() != LexicalType.THEN) {
				return false;
			}

			lu = env.getInput().get();
			if (lu.getType() != LexicalType.NL) {
				return false;
			}

			lu = env.getInput().get();
			env.getInput().unget(lu);
			stmt = StmtListNode.isMatch(env, lu);
			if (stmt == null || !stmt.Parse()) {
				return false;
			}
			nodeMap.put(cond, stmt);
			lu = env.getInput().get();
		}
		env.getInput().unget(lu);

		return true;
	}

	@Override
	public String toString() {
		String tmp = "IF [";

		for (Map.Entry<Node, Node> e : nodeMap.entrySet()) {
			tmp += "" + e.getKey() + "[" + e.getValue() + "]";
		}

		tmp += "]";

		return tmp;
	}

	@Override
	public Value getValue() {
		for (Map.Entry<Node, Node> e : nodeMap.entrySet()) {
			if (e.getKey() == null) {
				return e.getValue().getValue();
			} else if (e.getKey().getValue().getBValue()) {
				return e.getValue().getValue();
			}
		}
		return null;
	}
}