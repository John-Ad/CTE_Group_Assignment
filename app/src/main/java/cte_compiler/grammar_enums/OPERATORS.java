package cte_compiler.grammar_enums;

public enum OPERATORS {
    PLUS("+"),
    MINUS("-"),
    DIV("/"),
    MULT("*");

    private final String text;

    OPERATORS(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static OPERATORS getOperatorType(String op) {
        switch (op) {
            case "+":
                return PLUS;
            case "-":
                return MINUS;
            case "/":
                return DIV;
            case "*":
                return MULT;
        }
        return null;
    }
}
