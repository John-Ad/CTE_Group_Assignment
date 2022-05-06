package cte_compiler.icr_generation;

import cte_compiler.grammar_enums.OPERATORS;

public class ThreeAddressCode {
    private String referenceName;
    private OPERATORS operator;
    private TACArg firstArg;
    private TACArg secondArg;

    public ThreeAddressCode(String referenceName, OPERATORS operator, TACArg firstArg, TACArg secondArg) {
        this.referenceName = referenceName;
        this.operator = operator;
        this.firstArg = firstArg;
        this.secondArg = secondArg;
    }

    public OPERATORS getOperator() {
        return operator;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public TACArg getFirstArg() {
        return firstArg;
    }

    public TACArg getSecondArg() {
        return secondArg;
    }

    @Override
    public String toString() {
        return this.referenceName + " = " + this.firstArg.value + " " + this.operator.toString() + " "
                + this.secondArg.value;
    }

}
