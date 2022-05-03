package cte_compiler.icr_generation;

public class ThreeAddressCode {
    private String referenceName;
    private String operator;
    private TACArg firstArg;
    private TACArg secondArg;

    public ThreeAddressCode(String referenceName, String operator, TACArg firstArg, TACArg secondArg) {
        this.referenceName = referenceName;
        this.operator = operator;
        this.firstArg = firstArg;
        this.secondArg = secondArg;
    }

    public String getOperator() {
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
        return this.referenceName + " = " + this.firstArg.value + " " + this.operator + " " + this.secondArg.value;
    }

}
