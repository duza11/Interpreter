package newlang4;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LexicalAnalyzerImpl implements LexicalAnalyzer {

	PushbackReader source;
	ArrayDeque<LexicalUnit> luBuffer = new ArrayDeque<>();
	Map<String, LexicalUnit> reservedMap = new HashMap<String, LexicalUnit>() {
		{
			put("IF", new LexicalUnit(LexicalType.IF));
			put("THEN", new LexicalUnit(LexicalType.THEN));
			put("ELSE", new LexicalUnit(LexicalType.ELSE));
			put("ELSEIF", new LexicalUnit(LexicalType.ELSEIF));
			put("ENDIF", new LexicalUnit(LexicalType.ENDIF));
			put("FOR", new LexicalUnit(LexicalType.FOR));
			put("FORALL", new LexicalUnit(LexicalType.FORALL));
			put("NEXT", new LexicalUnit(LexicalType.NEXT));
			put("=", new LexicalUnit(LexicalType.EQ));
			put("<", new LexicalUnit(LexicalType.LT));
			put(">", new LexicalUnit(LexicalType.GT));
			put("<=", new LexicalUnit(LexicalType.LE));
			put("=<", new LexicalUnit(LexicalType.LE));
			put(">=", new LexicalUnit(LexicalType.GE));
			put("=>", new LexicalUnit(LexicalType.GE));
			put("<>", new LexicalUnit(LexicalType.NE));
			put("SUB", new LexicalUnit(LexicalType.FUNC));
			put("DIM", new LexicalUnit(LexicalType.DIM));
			put("AS", new LexicalUnit(LexicalType.AS));
			put("END", new LexicalUnit(LexicalType.END));
			put("\r\n", new LexicalUnit(LexicalType.NL));
			put(".", new LexicalUnit(LexicalType.DOT));
			put("WHILE", new LexicalUnit(LexicalType.WHILE));
			put("DO", new LexicalUnit(LexicalType.DO));
			put("UNTIL", new LexicalUnit(LexicalType.UNTIL));
			put("+", new LexicalUnit(LexicalType.ADD));
			put("-", new LexicalUnit(LexicalType.SUB));
			put("*", new LexicalUnit(LexicalType.MUL));
			put("/", new LexicalUnit(LexicalType.DIV));
			put(")", new LexicalUnit(LexicalType.RP));
			put("(", new LexicalUnit(LexicalType.LP));
			put(",", new LexicalUnit(LexicalType.COMMA));
			put("LOOP", new LexicalUnit(LexicalType.LOOP));
			put("TO", new LexicalUnit(LexicalType.TO));
			put("WEND", new LexicalUnit(LexicalType.WEND));
		}
	};

	public LexicalAnalyzerImpl(InputStream is) {
		// source = new BufferedReader(new InputStreamReader(is));
		Reader reader = new InputStreamReader(is);
		source = new PushbackReader(reader);
	}

	@Override
	public LexicalUnit get() throws Exception {
		if (!luBuffer.isEmpty()) {
			return luBuffer.poll();
		}

		while (true) {
			int ci = source.read();

			if (ci == -1) {
				return new LexicalUnit(LexicalType.EOF);
			}

			char c = (char) ci;

			if (c == ' ' || c == '\t') {
				continue;
			}

			if (isAlpha(c)) {
				return getAlpha(c);
			} else if (isNumeric(c)) {
				return getNumeric(c);
			} else if (c == '"') {
				return getLiteral(c);
			} else {
				return getSpecial(c);
			}
		}
	}

	@Override
	public boolean expect(LexicalType type) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public void unget(LexicalUnit token) {
		luBuffer.add(token);
	}

	private LexicalUnit getAlpha(char c) throws Exception {
		String res = "";

		while (true) {
			res += c;

			int ci = source.read();
			if (ci == -1) {
				break;
			}
			c = (char) ci;
			if (!isAlpha(c) && !isNumeric(c)) {
				source.unread(ci);
				break;
			}
		}

		return (reservedMap.containsKey(res) ? reservedMap.get(res)
				: new LexicalUnit(LexicalType.NAME, new ValueImpl(res, ValueType.STRING)));
	}

	private LexicalUnit getNumeric(char c) throws Exception {
		String res = "";
		boolean dotEnable = true;

		while (true) {
			res += c;

			int ci = source.read();
			if (ci == -1) {
				break;
			}

			c = (char) ci;

			if (c == '.' && dotEnable) {
				dotEnable = false;
				continue;
			}
			if (!isNumeric(c)) {
				source.unread(ci);
				break;
			}
		}

		return (res.matches("([1-9][0-9]*)|0")
				? new LexicalUnit(LexicalType.INTVAL, new ValueImpl(res, ValueType.INTEGER))
				: new LexicalUnit(LexicalType.DOUBLEVAL, new ValueImpl(res, ValueType.DOUBLE)));
	}

	private LexicalUnit getLiteral(char c) throws Exception {
		String res = "";

		while (true) {
			int ci = source.read();
			if (ci == -1) {
				break;
			}
			c = (char) ci;
			if (c == '"') {
				break;
			}

			res += c;
		}

		return new LexicalUnit(LexicalType.LITERAL, new ValueImpl(res, ValueType.STRING));
	}

	private LexicalUnit getSpecial(char c) throws Exception {
		String res = "";
		List<String> keys = new ArrayList<String>();

		for (Map.Entry<String, LexicalUnit> e : reservedMap.entrySet()) {
			if (c == e.getKey().charAt(0)) {
				keys.add(e.getKey());
			}
		}

		for (int i = 0;; i++) {
			res += c;

			for (int j = 0; j < keys.size(); j++) {
				try {
					if (c != keys.get(j).charAt(i)) {
						keys.remove(j);
						j--;
					}
				} catch (StringIndexOutOfBoundsException se) {
					keys.remove(j);
					j--;
					continue;
				}
			}

			if (keys.isEmpty()) {
				return searchSymbol(res);
			}

			int ci = source.read();
			if (ci == -1) {
				return reservedMap.get(res);
			}
			c = (char) ci;
		}
	}

	private LexicalUnit searchSymbol(String key) throws Exception {
		source.unread(key.charAt(key.length() - 1));
		key = key.substring(0, key.length() - 1);
		if (reservedMap.containsKey(key)) {
			return reservedMap.get(key);
		}
		return searchSymbol(key);
	}

	private boolean isAlpha(char c) {
		if (c >= 'a' && c <= 'z')
			return true;
		if (c >= 'A' && c <= 'Z')
			return true;
		return false;
	}

	private boolean isNumeric(char c) {
		if (c >= '0' && c <= '9')
			return true;
		return false;
	}
}