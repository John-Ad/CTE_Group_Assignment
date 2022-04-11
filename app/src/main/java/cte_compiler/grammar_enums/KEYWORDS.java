package cte_compiler.grammar_enums;

public enum KEYWORDS {
    PRINT("print"),
    VAR("var"),
    IF("if"),
    THEN("then"),
    WHILE("while"),
    DO("do"),
    END("end");

    private final String text;

    KEYWORDS(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
