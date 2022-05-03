package cte_compiler.icr_generation;

import cte_compiler.tokenizer.TOKEN_TYPES;

public class TACArg {
    public String value;
    public TOKEN_TYPES type;

    public TACArg(String value, TOKEN_TYPES type) {
        this.value = value;
        this.type = type;
    }

}
