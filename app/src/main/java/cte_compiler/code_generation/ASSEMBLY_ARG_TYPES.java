package cte_compiler.code_generation;

import cte_compiler.icr_generation.TAC_ARG_TYPES;

public enum ASSEMBLY_ARG_TYPES {
    CONSTANT,
    REGISTER;

    public static ASSEMBLY_ARG_TYPES tacArgConversion(TAC_ARG_TYPES arg) {
        switch (arg) {
            case CONSTANT:
                return CONSTANT;
            case REFERENCE:
                return REGISTER;
            default:
                return null;
        }
    }
}
