package cte_compiler.tokenizer;

public class Token {
    public TOKEN_TYPES type;
    public String value;
    public int lineNumber;

    public Token(TOKEN_TYPES type, String value, int lineNumber) {
        this.type = type;
        this.value = value;
        this.lineNumber = lineNumber;
    }
}
