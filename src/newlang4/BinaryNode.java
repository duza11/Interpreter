package newlang4;

import java.util.LinkedHashMap;
import java.util.Map;

public class BinaryNode extends Node {
	private Node left_operand;
	private Node right_operand;
	private Operator operator;
	private final Map<Operator, String> operatorMap = new LinkedHashMap<Operator, String>() {
		{
			put(Operator.ADD, "+");
			put(Operator.SUB, "-");
			put(Operator.MUL, "*");
			put(Operator.DIV, "/");
		}
	};

	public BinaryNode(Node left, Node right, Operator operator) {
		this.left_operand = left;
		this.right_operand = right;
		this.operator = operator;
	}

	@Override
	public String toString() {
		return operatorMap.get(operator) + "[" + left_operand + ", "
				+ right_operand + "]";
	}

	@Override
	public ValueType getType() {
		if (left_operand.getType() == ValueType.STRING
				|| right_operand.getType() == ValueType.STRING) {
			return ValueType.STRING;
		}
		if (left_operand.getType() == ValueType.DOUBLE
				|| right_operand.getType() == ValueType.DOUBLE) {
			return ValueType.DOUBLE;
		}
		return ValueType.INTEGER;
	}

	@Override
	public Value getValue() {
		if (getType() == ValueType.STRING) {
			switch (operator) {
			case ADD:
				return new ValueImpl(left_operand.getValue().getSValue()
						+ right_operand.getValue().getSValue(),
						ValueType.STRING);
			}
		}

		if (getType() == ValueType.DOUBLE) {
			switch (operator) {
			case ADD:
				return new ValueImpl(left_operand.getValue().getDValue()
						+ right_operand.getValue().getDValue(),
						ValueType.DOUBLE);
			case SUB:
				return new ValueImpl(left_operand.getValue().getDValue()
						- right_operand.getValue().getDValue(),
						ValueType.DOUBLE);
			case MUL:
				return new ValueImpl(left_operand.getValue().getDValue()
						* right_operand.getValue().getDValue(),
						ValueType.DOUBLE);
			case DIV:
				return new ValueImpl(left_operand.getValue().getDValue()
						/ right_operand.getValue().getDValue(),
						ValueType.DOUBLE);
			}
		}
		switch (operator) {
		case ADD:
			return new ValueImpl(left_operand.getValue().getIValue()
					+ right_operand.getValue().getIValue(), ValueType.INTEGER);
		case SUB:
			return new ValueImpl(left_operand.getValue().getIValue()
					- right_operand.getValue().getIValue(), ValueType.INTEGER);
		case MUL:
			return new ValueImpl(left_operand.getValue().getIValue()
					* right_operand.getValue().getIValue(), ValueType.INTEGER);
		case DIV:
			return new ValueImpl(left_operand.getValue().getIValue()
					/ right_operand.getValue().getIValue(), ValueType.INTEGER);
		}
		return super.getValue();
	}
}
