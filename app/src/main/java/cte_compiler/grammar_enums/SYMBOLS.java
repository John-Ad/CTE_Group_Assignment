package cte_compiler.grammar_enums;

public enum SYMBOLS {
    ASSIGNMENT("="),
    END_OF_STATEMENT(";");

    private final String text;

    SYMBOLS(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
