package cte_compiler.code_generation;

public class AssemblyArg {
    public String value;
    public ASSEMBLY_ARG_TYPES type;

    public AssemblyArg(String value, ASSEMBLY_ARG_TYPES type) {
        this.value = value;
        this.type = type;
    }
}
