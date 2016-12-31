package newlang4;

public class AbsFunction extends Function {
	public AbsFunction() {
		// TODO 自動生成されたコンストラクター・スタブ
    }

    public Value invoke(ExprListNode arg) {
    	Value v = arg.getList().get(0).getValue();
    	switch (v.getType()) {
		case INTEGER:
			return new ValueImpl(Math.abs(v.getIValue()), v.getType());
		case DOUBLE:
			return new ValueImpl(Math.abs(v.getDValue()), v.getType());
		default :
			return v;
		}
    }
}
