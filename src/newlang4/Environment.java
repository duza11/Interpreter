package newlang4;

import java.util.Hashtable;

public class Environment {
	private LexicalAnalyzer input;
	private Hashtable library;
	private Hashtable var_table;

	public Environment(LexicalAnalyzer my_input) {
		input = my_input;
		library = new Hashtable();
		library.put("PRINT", new PrintFunction());
		library.put("ABS", new AbsFunction());
		var_table = new Hashtable();
	}

	public LexicalAnalyzer getInput() {
		return input;
	}

	public Function getFunction(String fname) {
        return (Function) library.get(fname);
    }

	public VariableNode getVariable(String vname) {
		VariableNode v;
		v = (VariableNode) var_table.get(vname);
		if (v == null) {
			v = new VariableNode(vname);
			var_table.put(vname, v);
		}
		return v;
	}
}