package newlang4;

public class PrintFunction extends Function {
    public Value invoke(ExprListNode arg) {
    	arg.getList().forEach(node -> System.out.println(node.getValue().getSValue()));
    	return null;
    }
}
