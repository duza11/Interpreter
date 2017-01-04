package newlang4;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

public class ExprNode extends Node {
	private Node left;
	private Node right;
	private Node body;
	private ArrayDeque<Node> nodeStack = new ArrayDeque<Node>();
	private ArrayDeque<Operator> operatorStack = new ArrayDeque<>();
	private static Set<LexicalType> firstSet = new HashSet<LexicalType>();
	static {
		firstSet.add(LexicalType.SUB);
		firstSet.add(LexicalType.LP);
		firstSet.add(LexicalType.NAME);
		firstSet.add(LexicalType.INTVAL);
		firstSet.add(LexicalType.DOUBLEVAL);
		firstSet.add(LexicalType.LITERAL);
	};

	private ExprNode(Environment env) {
		this.env = env;
		super.type = NodeType.EXPR;
	}

	public static Node isMatch(Environment env, LexicalUnit first) {
		if (firstSet.contains(first.getType())) {
			return new ExprNode(env);
		}
		return null;
	}

	public boolean Parse() throws Exception {
		while (true) {
			Node operand = getOperand();
			if (operand == null) {
				return false;
			}
			nodeStack.push(operand);

			Operator operator = getOperator();
			if (operator == null) {
				break;
			}
			while (!operatorStack.isEmpty()) {
				if (operatorStack.peek() == Operator.MUL || operatorStack.peek() == Operator.DIV
						|| operator == Operator.ADD || operator == Operator.SUB) {
					reduce();
					continue;
				}
				break;
			}
			operatorStack.push(operator);
		}
		while (!operatorStack.isEmpty()) {
			reduce();
		}
		return true;
	}

	private Node getOperand() throws Exception {
		LexicalUnit lu = env.getInput().get();
		if (lu.getType() == LexicalType.LP) {
			lu = env.getInput().get();
			env.getInput().unget(lu);
			body = ExprNode.isMatch(env, lu);
			if (body == null || !body.Parse()) {
				return null;
			}

			lu = env.getInput().get();
			if (lu.getType() == LexicalType.RP) {
				return body;
			}
			return null;
		}

		if (lu.getType() == LexicalType.SUB) {
			lu = env.getInput().get();
			env.getInput().unget(lu);
			body = UnaryNode.isMatch(env, lu);
			if (body != null && body.Parse()) {
				return body;
			}
			return null;
		}

		body = IntConstantNode.isMatch(env, lu);
		if (body != null) {
			return body;
		}

		body = DoubleConstantNode.isMatch(env, lu);
		if (body != null) {
			return body;
		}

		body = StringConstantNode.isMatch(env, lu);
		if (body != null) {
			return body;
		}

		env.getInput().unget(lu);
		body = CallFuncNode.isMatch(env, lu);
		if (body != null && body.Parse()) {
			return body;
		}

		lu = env.getInput().get();
		body = VariableNode.isMatch(env, lu);
		if (body != null) {
			return body;
		}

		return null;
	}

	private Operator getOperator() throws Exception {
		LexicalUnit lu = env.getInput().get();
		switch (lu.getType()) {
		case ADD:
			return Operator.ADD;
		case SUB:
			return Operator.SUB;
		case MUL:
			return Operator.MUL;
		case DIV:
			return Operator.DIV;
		default:
			env.getInput().unget(lu);
			return null;
		}
	}

	private void reduce() {
		right = nodeStack.pop();
		left = nodeStack.pop();
		nodeStack.push(new BinaryNode(left, right, operatorStack.pop()));
	}

	@Override
	public String toString() {
		return nodeStack.peek().toString();
	}

	@Override
	public Value getValue() {
		return nodeStack.peek().getValue();
	}
}
