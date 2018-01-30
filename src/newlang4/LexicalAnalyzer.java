package newlang4;

public interface LexicalAnalyzer {
    public LexicalUnit get() throws Exception;
    public boolean expect(LexicalType type);
    public void unget(LexicalUnit token);
}
