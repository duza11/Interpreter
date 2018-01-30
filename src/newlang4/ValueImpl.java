package newlang4;

public class ValueImpl implements Value {

	private String s;
	private int i;
	private double d;
	private boolean b;
	private ValueType type;

	public ValueImpl(String s) {
		this.s = s;
	}

	public ValueImpl(int i) {
		this.i = i;
	}

	public ValueImpl(double d) {
		this.d = d;
	}

	public ValueImpl(boolean b) {
		this.b = b;
	}

	public ValueImpl(String s, ValueType type) {
		this.s = s;
		this.type = type;
	}

	public ValueImpl(int i, ValueType type) {
		this.s = Integer.toString(i);
		this.type = type;
	}

	public ValueImpl(double d, ValueType type) {
		this.s = Double.toString(d);
		this.type = type;
	}

	public ValueImpl(boolean b, ValueType type) {
		this.s = Boolean.toString(b);
		this.type = type;
	}

	@Override
	public String getSValue() {
		return s;
	}

	@Override
	public int getIValue() {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	@Override
	public double getDValue() {
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return 0.0;
		}
	}

	@Override
	public boolean getBValue() {
		return Boolean.parseBoolean(s);
	}

	@Override
	public ValueType getType() {
		return this.type;
	}

}