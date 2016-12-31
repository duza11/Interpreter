package newlang4;

public class PrintFunction extends Function {
	public PrintFunction() {
		// TODO 自動生成されたコンストラクター・スタブ
    }

    public Value invoke(ExprListNode arg) {
    	arg.getList().forEach(node -> System.out.println(node.getValue().getSValue()));
    	return null;
    }
}
