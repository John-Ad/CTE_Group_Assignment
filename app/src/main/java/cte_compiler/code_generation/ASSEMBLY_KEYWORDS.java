package cte_compiler.code_generation;

import cte_compiler.grammar_enums.OPERATORS;

public enum ASSEMBLY_KEYWORDS {
    MOV("MOV"),
    MUL("IMUL"),
    DIV("DIV"),
    ADD("ADD"),
    SUB("SUB");

    private final String text;

    ASSEMBLY_KEYWORDS(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static ASSEMBLY_KEYWORDS getKeywordForOp(OPERATORS op) {
        switch (op) {
            case PLUS:
                return ADD;
            case DIV:
                return DIV;
            case MINUS:
                return SUB;
            case MULT:
                return MUL;
            default:
                return null;
        }
    }
}
