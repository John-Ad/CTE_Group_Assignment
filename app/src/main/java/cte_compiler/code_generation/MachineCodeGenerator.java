package cte_compiler.code_generation;

import java.util.HashMap;

public class MachineCodeGenerator {
    private HashMap<String, String> movOpcodeMappings;
    private HashMap<String, String> addOpcodeMappings;

    public MachineCodeGenerator() {
        this.movOpcodeMappings = new HashMap<String, String>();
        this.addOpcodeMappings = new HashMap<String, String>();

        // mov opcode mappings
        this.movOpcodeMappings.put("eax", "10111000");
        this.movOpcodeMappings.put("ebx", "10111011");
        this.movOpcodeMappings.put("ecx", "10111001");
        this.movOpcodeMappings.put("edx", "10111010");

        // add opcode mappings
        this.addOpcodeMappings.put("eax", "10000011 11000000");
        this.addOpcodeMappings.put("ebx", "10000011 11000011");
        this.addOpcodeMappings.put("ecx", "10000011 11000001");
        this.addOpcodeMappings.put("edx", "10000011 11000010");

    }
}
