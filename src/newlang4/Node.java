package newlang4;

public class Node {
    protected NodeType type;
    protected Environment env;

    public Node next;

    /** Creates a new instance of Node */
    public Node() {
    }
    public Node(NodeType my_type) {
        type = my_type;
    }
    public Node(Environment my_env) {
        env = my_env;
    }

    public ValueType getType() {
        return null;
    }

    public boolean Parse() throws Exception {
        return true;
    }

    public Value getValue() {
        return null;
    }

    public String toString() {
    	if (type == NodeType.END) return "END";
    	else return "Node";
    }

}
