package cte_compiler.code_generation;

import java.util.ArrayList;
import java.util.HashMap;

public class RegisterPool {

    private int poolSize;
    private ArrayList<String> inUseStack;
    private ArrayList<String> freeStack;

    public RegisterPool() {
        this.poolSize = 4;
        this.inUseStack = new ArrayList<String>();
        this.freeStack = new ArrayList<String>();

        // add general purpose registers
        this.freeStack.add("r8d");
        this.freeStack.add("r9d");
        this.freeStack.add("r10d");
        this.freeStack.add("r11d");
        this.freeStack.add("r12d");
        this.freeStack.add("r13d");
        this.freeStack.add("r14d");
        this.freeStack.add("r15d");
    }

    public String getRegister() {
        if (this.freeStack.size() == 0)
            return "Memory error";

        String reg = freeStack.remove(0);
        inUseStack.add(reg);
        return reg;
    }

    public boolean freeRegister(String reg) {
        if (this.inUseStack.remove(reg)) {
            this.freeStack.add(reg);
            return true;
        }

        return false;
    }

}
