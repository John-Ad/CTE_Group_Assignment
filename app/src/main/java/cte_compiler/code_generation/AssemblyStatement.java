package cte_compiler.code_generation;

public class AssemblyStatement {
    public ASSEMBLY_KEYWORDS instruction;
    public AssemblyArg arg1;
    public AssemblyArg arg2;
    public AssemblyArg arg3;

    public AssemblyStatement(ASSEMBLY_KEYWORDS instruction, AssemblyArg arg1, AssemblyArg arg2) {
        this.instruction = instruction;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = null;
    }

    public AssemblyStatement() {
        this.instruction = null;
        this.arg1 = null;
        this.arg2 = null;
        this.arg3 = null;
    }

}
