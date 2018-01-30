package newlang4;

import java.util.HashSet;
import java.util.Set;

public class LoopNode extends Node {
	private Node front_cond = null;
	private Node back_cond = null;
	private Node stmtList;
	private LexicalType loopType;
	private static Set<LexicalType> firstSet = new HashSet<LexicalType>();
	private LexicalUnit lu;
	static {
		firstSet.add(LexicalType.WHILE);
		firstSet.add(LexicalType.DO);
	};

	private LoopNode(Environment env) {
		this.env = env;
		super.type = NodeType.LOOP_BLOCK;
	}

	public static Node isMatch(Environment env, LexicalUnit first) {
		if (firstSet.contains(first.getType())) {
			return new LoopNode(env);
		}
		return null;
	}

	@Override
	public boolean Parse() throws Exception {
		lu = env.getInput().get();
		if (lu.getType() == LexicalType.WHILE) {
			return isWhile();
		}

		if (lu.getType() == LexicalType.DO) {
			return isDoLoop();
		}

		return false;
	}

	private boolean isWhile() throws Exception {
		loopType = lu.getType();

		lu = env.getInput().get();
		env.getInput().unget(lu);
		front_cond = CondNode.isMatch(env, lu);
		if (front_cond == null || !front_cond.Parse()) {
			return false;
		}

		lu = env.getInput().get();
		if (lu.getType() != LexicalType.NL) {
			return false;
		}

		lu = env.getInput().get();
		env.getInput().unget(lu);
		stmtList = StmtListNode.isMatch(env, lu);
		if (stmtList == null || !stmtList.Parse()) {
			return false;
		}

		lu = env.getInput().get();
		if (lu.getType() != LexicalType.WEND) {
			return false;
		}

		lu = env.getInput().get();
		if (lu.getType() != LexicalType.NL) {
			return false;
		}
		return true;
	}

	private boolean isDoLoop() throws Exception {
		lu = env.getInput().get();
		if (lu.getType() == LexicalType.WHILE || lu.getType() == LexicalType.UNTIL) {
			return isDoCondLoop();
		}

		if (lu.getType() == LexicalType.NL) {
			return isDoLoopCond();
		}
		return false;
	}

	private boolean isDoCondLoop() throws Exception {
		loopType = lu.getType();
		lu = env.getInput().get();
		env.getInput().unget(lu);
		front_cond = CondNode.isMatch(env, lu);
		if (front_cond == null || !front_cond.Parse()) {
			return false;
		}

		lu = env.getInput().get();
		if (lu.getType() != LexicalType.NL) {
			return false;
		}

		lu = env.getInput().get();
		env.getInput().unget(lu);
		stmtList = StmtListNode.isMatch(env, lu);
		if (stmtList == null || !stmtList.Parse()) {
			return false;
		}

		lu = env.getInput().get();
		if (lu.getType() != LexicalType.LOOP) {
			return false;
		}

		lu = env.getInput().get();
		if (lu.getType() != LexicalType.NL) {
			return false;
		}
		return true;
	}

	private boolean isDoLoopCond() throws Exception {
		lu = env.getInput().get();
		env.getInput().unget(lu);
		stmtList = StmtListNode.isMatch(env, lu);
		if (stmtList == null || !stmtList.Parse()) {
			return false;
		}

		lu = env.getInput().get();
		if (lu.getType() != LexicalType.LOOP) {
			return false;
		}

		lu = env.getInput().get();

		loopType = lu.getType();
		if (loopType != LexicalType.WHILE && loopType != LexicalType.UNTIL) {
			return false;
		}

		lu = env.getInput().get();
		env.getInput().unget(lu);
		back_cond = CondNode.isMatch(env, lu);
		if (back_cond == null || !back_cond.Parse()) {
			return false;
		}

		lu = env.getInput().get();
		if (lu.getType() != LexicalType.NL) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "LOOP [" + ((front_cond == null) ?  "" : front_cond.toString()) + "[" + stmtList.toString() + "]["
				+ ((back_cond == null) ? "" : back_cond.toString()) + "]]";
	}

	@Override
	public Value getValue() {
		if (front_cond != null) {
			switch (loopType) {
			case WHILE:
				while (front_cond.getValue().getBValue()) {
					stmtList.getValue();
				}
				break;
			default:
				while (!front_cond.getValue().getBValue()) {
					stmtList.getValue();
				}
				break;
			}
		} else {
			switch (loopType) {
			case WHILE:
				do {
					stmtList.getValue();
				} while (back_cond.getValue().getBValue());
				break;
			default:
				do {
					stmtList.getValue();
				} while (!back_cond.getValue().getBValue());
				break;
			}
		}
		return super.getValue();
	}
}