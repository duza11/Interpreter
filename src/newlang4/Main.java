package newlang4;

import java.io.FileInputStream;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		FileInputStream fin = null;
		LexicalAnalyzer lex;
		LexicalUnit first;
		Environment env;
		Node program;

		System.out.println("basic parser");
		try {
			fin = new FileInputStream("test.txt");
		} catch (Exception e) {
			System.out.println("file not found");
			System.exit(-1);
		}
		lex = new LexicalAnalyzerImpl(fin);
		env = new Environment(lex);
		first = lex.get();
		lex.unget(first);

		program = ProgramNode.isMatch(env, first);
		if (program != null && program.Parse()) {
			System.out.println(program);
			program.getValue();
			// System.out.println("value = " + program.getValue());
		} else {
			System.out.println("syntax error");
		}
	}

}

/*
 * BlockNode > LoopNode
 * Parseメソッドでは自分の担当部分までは読み進める
 * 構文エラーでfalseを返す
 * isMatchでfirst集合のチェック，担当部分だったら処理するオブジェクトを返す
 */