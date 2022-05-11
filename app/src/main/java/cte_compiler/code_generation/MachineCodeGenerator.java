package cte_compiler.code_generation;

import java.util.ArrayList;
import java.util.HashMap;

public class MachineCodeGenerator {
    private ArrayList<String> machineStatements;

    private HashMap<String, String> movOpcodeMappings;
    private HashMap<String, String> addOpcodeMappings;
    private HashMap<String, String> subOpcodeMappings;
    private HashMap<String, String> divOpcodeMappings;
    private HashMap<String, String> mulOpcodeMappings;

    public MachineCodeGenerator() {
        this.machineStatements = new ArrayList<String>();

        this.movOpcodeMappings = new HashMap<String, String>();
        this.addOpcodeMappings = new HashMap<String, String>();
        this.subOpcodeMappings = new HashMap<String, String>();
        this.divOpcodeMappings = new HashMap<String, String>();
        this.mulOpcodeMappings = new HashMap<String, String>();

        // ------------------------------
        // MOV OPCODE MAPPINGS
        // ------------------------------

        // move const to eax or eab
        this.movOpcodeMappings.put("eax", "10111000"); // for div and mul
        this.movOpcodeMappings.put("ebx", "10111011"); // for div and mul

        // move const to any r
        this.movOpcodeMappings.put("r8d", "11000001 101110000");
        this.movOpcodeMappings.put("r9d", "11000001 101110010");
        this.movOpcodeMappings.put("r10d", "11000001 101110100");
        this.movOpcodeMappings.put("r11d", "11000001 101110110");
        this.movOpcodeMappings.put("r12d", "11000001 101111000");
        this.movOpcodeMappings.put("r13d", "11000001 101111010");
        this.movOpcodeMappings.put("r14d", "11000001 101111100");
        this.movOpcodeMappings.put("r15d", "11000001 101111110");

        // move any r to eax
        this.movOpcodeMappings.put("eaxr8d", "1000100 10001001 11000000");
        this.movOpcodeMappings.put("eaxr9d", "1000100 10001001 11001000");
        this.movOpcodeMappings.put("eaxr10d", "1000100 10001001 11010000");
        this.movOpcodeMappings.put("eaxr11d", "1000100 10001001 11011000");
        this.movOpcodeMappings.put("eaxr12d", "1000100 10001001 11100000");
        this.movOpcodeMappings.put("eaxr13d", "1000100 10001001 11101000");
        this.movOpcodeMappings.put("eaxr14d", "1000100 10001001 11110000");
        this.movOpcodeMappings.put("eaxr15d", "1000100 10001001 11111000");

        // move eax to any r
        this.movOpcodeMappings.put("r8deax", "1000001 10001001 11000000");
        this.movOpcodeMappings.put("r9deax", "1000001 10001001 11000001");
        this.movOpcodeMappings.put("r10deax", "1000001 10001001 11000010");
        this.movOpcodeMappings.put("r11deax", "1000001 10001001 11000011");
        this.movOpcodeMappings.put("r12deax", "1000001 10001001 11000100");
        this.movOpcodeMappings.put("r13deax", "1000001 10001001 11000101");
        this.movOpcodeMappings.put("r14deax", "1000001 10001001 11000110");
        this.movOpcodeMappings.put("r15deax", "1000001 10001001 11000111");

        // ------------------------------
        // ADD OPCODE MAPPINGS
        // ------------------------------

        // add const to r
        this.addOpcodeMappings.put("r8d", "1001001 10000011 11000000");
        this.addOpcodeMappings.put("r9d", "1001001 10000011 11000001");
        this.addOpcodeMappings.put("r10d", "1001001 10000011 11000010");
        this.addOpcodeMappings.put("r11d", "1001001 10000011 11000011");
        this.addOpcodeMappings.put("r12d", "1001001 10000011 11000100");
        this.addOpcodeMappings.put("r13d", "1001001 10000011 11000101");
        this.addOpcodeMappings.put("r14d", "1001001 10000011 11000110");
        this.addOpcodeMappings.put("r15d", "1001001 10000011 11000111");

        // add r8 to any r
        this.addOpcodeMappings.put("r8dr8d", "11000101 1 110000000");
        this.addOpcodeMappings.put("r9dr8d", "11000101 1 110000011");
        this.addOpcodeMappings.put("r10dr8d", "11000101 1 110000100");
        this.addOpcodeMappings.put("r11dr8d", "11000101 1 110000111");
        this.addOpcodeMappings.put("r12dr8d", "11000101 1 110001000");
        this.addOpcodeMappings.put("r13dr8d", "11000101 1 110001011");
        this.addOpcodeMappings.put("r14dr8d", "11000101 1 110001100");
        this.addOpcodeMappings.put("r15dr8d", "11000101 1 110001111");

        // add r9 to any r
        this.addOpcodeMappings.put("r8dr9d", "11000101 1 110010000");
        this.addOpcodeMappings.put("r9dr9d", "11000101 1 110010011");
        this.addOpcodeMappings.put("r10dr9d", "11000101 1 110010100");
        this.addOpcodeMappings.put("r11dr9d", "11000101 1 110010111");
        this.addOpcodeMappings.put("r12dr9d", "11000101 1 110011000");
        this.addOpcodeMappings.put("r13dr9d", "11000101 1 110011011");
        this.addOpcodeMappings.put("r14dr9d", "11000101 1 110011100");
        this.addOpcodeMappings.put("r15dr9d", "11000101 1 110011111");

        // add r10 to any r
        this.addOpcodeMappings.put("r8dr10d", "1000101 1 11010000");
        this.addOpcodeMappings.put("r9dr10d", "1000101 1 11010001");
        this.addOpcodeMappings.put("r10dr10d", "1000101 1 11010010");
        this.addOpcodeMappings.put("r11dr10d", "1000101 1 11010011");
        this.addOpcodeMappings.put("r12dr10d", "1000101 1 11010100");
        this.addOpcodeMappings.put("r13dr10d", "1000101 1 11010101");
        this.addOpcodeMappings.put("r14dr10d", "1000101 1 11010110");
        this.addOpcodeMappings.put("r15dr10d", "1000101 1 11010111");

        // add r11 to any r
        this.addOpcodeMappings.put("r8dr11d", "11000101 1 110110000");
        this.addOpcodeMappings.put("r9dr11d", "11000101 1 110110011");
        this.addOpcodeMappings.put("r10dr11d", "11000101 1 110110100");
        this.addOpcodeMappings.put("r11dr11d", "11000101 1 110110111");
        this.addOpcodeMappings.put("r12dr11d", "11000101 1 110111000");
        this.addOpcodeMappings.put("r13dr11d", "11000101 1 110111011");
        this.addOpcodeMappings.put("r14dr11d", "11000101 1 110111100");
        this.addOpcodeMappings.put("r15dr11d", "11000101 1 110111111");

        // add r12 to any r
        this.addOpcodeMappings.put("r8dr12d", "1000101 1 11100000");
        this.addOpcodeMappings.put("r9dr12d", "1000101 1 11100001");
        this.addOpcodeMappings.put("r10dr12d", "1000101 1 11100010");
        this.addOpcodeMappings.put("r11dr12d", "1000101 1 11100011");
        this.addOpcodeMappings.put("r12dr12d", "1000101 1 11100100");
        this.addOpcodeMappings.put("r13dr12d", "1000101 1 11100101");
        this.addOpcodeMappings.put("r14dr12d", "1000101 1 11100110");
        this.addOpcodeMappings.put("r15dr12d", "1000101 1 11100111");

        // add r13 to any r
        this.addOpcodeMappings.put("r8dr13d", "1000101 1 11101000");
        this.addOpcodeMappings.put("r9dr13d", "1000101 1 11101001");
        this.addOpcodeMappings.put("r10dr13d", "1000101 1 11101010");
        this.addOpcodeMappings.put("r11dr13d", "1000101 1 11101011");
        this.addOpcodeMappings.put("r12dr13d", "1000101 1 11101100");
        this.addOpcodeMappings.put("r13dr13d", "1000101 1 11101101");
        this.addOpcodeMappings.put("r14dr13d", "1000101 1 11101110");
        this.addOpcodeMappings.put("r15dr13d", "1000101 1 11101111");

        // add r14 to any r
        this.addOpcodeMappings.put("r8dr14d", "1000101 1 11110000");
        this.addOpcodeMappings.put("r9dr14d", "1000101 1 11110001");
        this.addOpcodeMappings.put("r10dr14d", "1000101 1 11110010");
        this.addOpcodeMappings.put("r11dr14d", "1000101 1 11110011");
        this.addOpcodeMappings.put("r12dr14d", "1000101 1 11110100");
        this.addOpcodeMappings.put("r13dr14d", "1000101 1 11110101");
        this.addOpcodeMappings.put("r14dr14d", "1000101 1 11110110");
        this.addOpcodeMappings.put("r15dr14d", "1000101 1 11110111");

        //
        // add r15 to any r
        this.addOpcodeMappings.put("r8dr15d", "1000101 1 11111000");
        this.addOpcodeMappings.put("r9dr15d", "1000101 1 11111001");
        this.addOpcodeMappings.put("r10dr15d", "1000101 1 11111010");
        this.addOpcodeMappings.put("r11dr15d", "1000101 1 11111011");
        this.addOpcodeMappings.put("r12dr15d", "1000101 1 11111100");
        this.addOpcodeMappings.put("r13dr15d", "1000101 1 11111101");
        this.addOpcodeMappings.put("r14dr15d", "1000101 1 11111110");
        this.addOpcodeMappings.put("r15dr15d", "1000101 1 11111111");

        // ------------------------------
        // SUB OPCODE MAPPINGS
        // ------------------------------

        this.subOpcodeMappings.put("eax", "10000011 11000010");
        this.subOpcodeMappings.put("ebx", "10000011 11000011");
        this.subOpcodeMappings.put("ecx", "10000011 11000001");
        this.subOpcodeMappings.put("edx", "10000011 11000010");

        // sub const from r
        this.subOpcodeMappings.put("r8d", "1000001 10000011 11101000");
        this.subOpcodeMappings.put("r9d", "1000001 10000011 11101001");
        this.subOpcodeMappings.put("r10d", "1000001 10000011 11101010");
        this.subOpcodeMappings.put("r11d", "1000001 10000011 11101011");
        this.subOpcodeMappings.put("r12d", "1000001 10000011 11101100");
        this.subOpcodeMappings.put("r13d", "1000001 10000011 11101101");
        this.subOpcodeMappings.put("r14d", "1000001 10000011 11101110");
        this.subOpcodeMappings.put("r15d", "1000001 10000011 11101111");

        // sub r8 from any r
        this.subOpcodeMappings.put("r8dr8d", "11000101 101001 110000000");
        this.subOpcodeMappings.put("r9dr8d", "11000101 101001 110000011");
        this.subOpcodeMappings.put("r10dr8d", "11000101 101001 110000100");
        this.subOpcodeMappings.put("r11dr8d", "11000101 101001 110000111");
        this.subOpcodeMappings.put("r12dr8d", "11000101 101001 110001000");
        this.subOpcodeMappings.put("r13dr8d", "11000101 101001 110001011");
        this.subOpcodeMappings.put("r14dr8d", "11000101 101001 110001100");
        this.subOpcodeMappings.put("r15dr8d", "11000101 101001 110001111");

        // sub r9 from any r
        this.subOpcodeMappings.put("r8dr9d", "11000101 101001 110010000");
        this.subOpcodeMappings.put("r9dr9d", "11000101 101001 110010011");
        this.subOpcodeMappings.put("r10dr9d", "11000101 101001 110010100");
        this.subOpcodeMappings.put("r11dr9d", "11000101 101001 110010111");
        this.subOpcodeMappings.put("r12dr9d", "11000101 101001 110011000");
        this.subOpcodeMappings.put("r13dr9d", "11000101 101001 110011011");
        this.subOpcodeMappings.put("r14dr9d", "11000101 101001 110011100");
        this.subOpcodeMappings.put("r15dr9d", "11000101 101001 110011111");

        // sub r10 from any r
        this.subOpcodeMappings.put("r8dr10d", "1000101 101001 11010000");
        this.subOpcodeMappings.put("r9dr10d", "1000101 101001 11010001");
        this.subOpcodeMappings.put("r10dr10d", "1000101 101001 11010010");
        this.subOpcodeMappings.put("r11dr10d", "1000101 101001 11010011");
        this.subOpcodeMappings.put("r12dr10d", "1000101 101001 11010100");
        this.subOpcodeMappings.put("r13dr10d", "1000101 101001 11010101");
        this.subOpcodeMappings.put("r14dr10d", "1000101 101001 11010110");
        this.subOpcodeMappings.put("r15dr10d", "1000101 101001 11010111");

        // sub r11 from any r
        this.subOpcodeMappings.put("r8dr11d", "11000101 101001 110110000");
        this.subOpcodeMappings.put("r9dr11d", "11000101 101001 110110011");
        this.subOpcodeMappings.put("r10dr11d", "11000101 101001 110110100");
        this.subOpcodeMappings.put("r11dr11d", "11000101 101001 110110111");
        this.subOpcodeMappings.put("r12dr11d", "11000101 101001 110111000");
        this.subOpcodeMappings.put("r13dr11d", "11000101 101001 110111011");
        this.subOpcodeMappings.put("r14dr11d", "11000101 101001 110111100");
        this.subOpcodeMappings.put("r15dr11d", "11000101 101001 110111111");

        // sub r12 from any r
        this.subOpcodeMappings.put("r8dr12d", "1000101 101001 11100000");
        this.subOpcodeMappings.put("r9dr12d", "1000101 101001 11100001");
        this.subOpcodeMappings.put("r10dr12d", "1000101 101001 11100010");
        this.subOpcodeMappings.put("r11dr12d", "1000101 101001 11100011");
        this.subOpcodeMappings.put("r12dr12d", "1000101 101001 11100100");
        this.subOpcodeMappings.put("r13dr12d", "1000101 101001 11100101");
        this.subOpcodeMappings.put("r14dr12d", "1000101 101001 11100110");
        this.subOpcodeMappings.put("r15dr12d", "1000101 101001 11100111");

        // sub r13 from any r
        this.subOpcodeMappings.put("r8dr13d", "1000101 101001 11101000");
        this.subOpcodeMappings.put("r9dr13d", "1000101 101001 11101001");
        this.subOpcodeMappings.put("r10dr13d", "1000101 101001 11101010");
        this.subOpcodeMappings.put("r11dr13d", "1000101 101001 11101011");
        this.subOpcodeMappings.put("r12dr13d", "1000101 101001 11101100");
        this.subOpcodeMappings.put("r13dr13d", "1000101 101001 11101101");
        this.subOpcodeMappings.put("r14dr13d", "1000101 101001 11101110");
        this.subOpcodeMappings.put("r15dr13d", "1000101 101001 11101111");

        // sub r14 from any r
        this.subOpcodeMappings.put("r8dr14d", "1000101 101001 11110000");
        this.subOpcodeMappings.put("r9dr14d", "1000101 101001 11110001");
        this.subOpcodeMappings.put("r10dr14d", "1000101 101001 11110010");
        this.subOpcodeMappings.put("r11dr14d", "1000101 101001 11110011");
        this.subOpcodeMappings.put("r12dr14d", "1000101 101001 11110100");
        this.subOpcodeMappings.put("r13dr14d", "1000101 101001 11110101");
        this.subOpcodeMappings.put("r14dr14d", "1000101 101001 11110110");
        this.subOpcodeMappings.put("r15dr14d", "1000101 101001 11110111");

        // sub r15 from any r
        this.subOpcodeMappings.put("r8dr15d", "1000101 101001 11111000");
        this.subOpcodeMappings.put("r9dr15d", "1000101 101001 11111001");
        this.subOpcodeMappings.put("r10dr15d", "1000101 101001 11111010");
        this.subOpcodeMappings.put("r11dr15d", "1000101 101001 11111011");
        this.subOpcodeMappings.put("r12dr15d", "1000101 101001 11111100");
        this.subOpcodeMappings.put("r13dr15d", "1000101 101001 11111101");
        this.subOpcodeMappings.put("r14dr15d", "1000101 101001 11111110");
        this.subOpcodeMappings.put("r15dr15d", "1000101 101001 11111111");

        // ------------------------------
        // DIV OPCODE MAPPINGS
        // ------------------------------
        this.divOpcodeMappings.put("div", "11110111 11110011"); // div eax by ebx

        // ------------------------------
        // MUL OPCODE MAPPINGS
        // ------------------------------
        this.mulOpcodeMappings.put("imul", "1101011 11000000"); // multiply eax by constant and store in eax
                                                                // this prevents the need to first move const to ebx and
                                                                // then mul

        // mul by const and store in a r
        this.mulOpcodeMappings.put("imulr8d", "1000100 1101011 11000000");
        this.mulOpcodeMappings.put("imulr9d", "1000100 1101011 11001000");
        this.mulOpcodeMappings.put("imulr10d", "1000100 1101011 11010000");
        this.mulOpcodeMappings.put("imulr11d", "1000100 1101011 11011000");
        this.mulOpcodeMappings.put("imulr12d", "1000100 1101011 11100000");
        this.mulOpcodeMappings.put("imulr13d", "1000100 1101011 11101000");
        this.mulOpcodeMappings.put("imulr14d", "1000100 1101011 11110000");
        this.mulOpcodeMappings.put("imulr15d", "1000100 1101011 11111000");
    }

    public boolean generateMachineCode(ArrayList<AssemblyStatement> assemblyStatements) {

        // return false if there are no assembly statements
        if (assemblyStatements.size() == 0) {
            return false;
        }

        // loop through assembly statements
        for (AssemblyStatement astmt : assemblyStatements) {

            String machineCode = "";

            // --- MOV OP ---
            if (astmt.instruction == ASSEMBLY_KEYWORDS.MOV) {

                // IF MOVING CONST TO REGISTER
                if (astmt.arg1.type == ASSEMBLY_ARG_TYPES.REGISTER && astmt.arg2.type == ASSEMBLY_ARG_TYPES.CONSTANT) {

                    // add start of mov op code
                    machineCode += movOpcodeMappings.get(astmt.arg1.value.toLowerCase());

                    // get binary of arg2
                    Integer arg2Val = Integer.parseInt(astmt.arg2.value);
                    String binary = Integer.toBinaryString(arg2Val);

                    // add constant value
                    machineCode += " " + binary;

                    // add to statements
                    this.machineStatements.add(machineCode);

                }

                // IF MOVING REGISTER TO REGISTER
                if (astmt.arg1.type == ASSEMBLY_ARG_TYPES.REGISTER && astmt.arg2.type == ASSEMBLY_ARG_TYPES.REGISTER) {

                    String regCombo = astmt.arg1.value.toLowerCase() + astmt.arg2.value.toLowerCase();

                    // op code using combo of the 2 registers
                    machineCode += movOpcodeMappings.get(regCombo);

                    // add to statements
                    this.machineStatements.add(machineCode);
                }
            }

            // --- DIV OP ---
            if (astmt.instruction == ASSEMBLY_KEYWORDS.DIV) {
                machineCode += divOpcodeMappings.get("div"); // div will always use ebx so no need for extra mappings

                // add to statements
                this.machineStatements.add(machineCode);
            }

            // --- MUL OP ---
            if (astmt.instruction == ASSEMBLY_KEYWORDS.MUL) {

                Integer argVal = 0;
                String binary = "";

                // add start of code

                if (astmt.arg3 == null) { // the 2 op regular imul is used
                    machineCode += mulOpcodeMappings.get("imul");
                    // get binary of arg2
                    argVal = Integer.parseInt(astmt.arg2.value);
                    binary = Integer.toBinaryString(argVal);
                } else {
                    machineCode += mulOpcodeMappings.get("imul" + astmt.arg1.value.toLowerCase());
                    // get binary of arg3
                    argVal = Integer.parseInt(astmt.arg3.value);
                    binary = Integer.toBinaryString(argVal);
                }

                // add constant
                machineCode += " " + binary;

                // add to statements
                this.machineStatements.add(machineCode);
            }

            // --- ADD OP ---
            if (astmt.instruction == ASSEMBLY_KEYWORDS.ADD) {

                // IF ADDING CONST TO REGISTER
                if (astmt.arg1.type == ASSEMBLY_ARG_TYPES.REGISTER && astmt.arg2.type == ASSEMBLY_ARG_TYPES.CONSTANT) {

                    // add start of add op code
                    machineCode += addOpcodeMappings.get(astmt.arg1.value.toLowerCase());

                    // get binary of arg2
                    Integer arg2Val = Integer.parseInt(astmt.arg2.value);
                    String binary = Integer.toBinaryString(arg2Val);

                    // add constant value
                    machineCode += " " + binary;

                    // add to statements
                    this.machineStatements.add(machineCode);

                }

                // IF ADDING REGISTER TO REGISTER
                if (astmt.arg1.type == ASSEMBLY_ARG_TYPES.REGISTER && astmt.arg2.type == ASSEMBLY_ARG_TYPES.REGISTER) {

                    String regCombo = astmt.arg1.value.toLowerCase() + astmt.arg2.value.toLowerCase();

                    // op code using combo of the 2 registers
                    machineCode += addOpcodeMappings.get(regCombo);

                    // add to statements
                    this.machineStatements.add(machineCode);
                }
            }

            // --- SUB OP ---
            if (astmt.instruction == ASSEMBLY_KEYWORDS.SUB) {

                // IF SUBTRACTING CONST FROM REGISTER
                if (astmt.arg1.type == ASSEMBLY_ARG_TYPES.REGISTER && astmt.arg2.type == ASSEMBLY_ARG_TYPES.CONSTANT) {

                    // add start of add op code
                    machineCode += subOpcodeMappings.get(astmt.arg1.value.toLowerCase());

                    // get binary of arg2
                    Integer arg2Val = Integer.parseInt(astmt.arg2.value);
                    String binary = Integer.toBinaryString(arg2Val);

                    // add constant value
                    machineCode += " " + binary;

                    // add to statements
                    this.machineStatements.add(machineCode);

                }

                // IF SUBTRACTING REGISTER FROM REGISTER
                if (astmt.arg1.type == ASSEMBLY_ARG_TYPES.REGISTER && astmt.arg2.type == ASSEMBLY_ARG_TYPES.REGISTER) {

                    String regCombo = astmt.arg1.value.toLowerCase() + astmt.arg2.value.toLowerCase();

                    // op code using combo of the 2 registers
                    machineCode += subOpcodeMappings.get(regCombo);

                    // add to statements
                    this.machineStatements.add(machineCode);
                }
            }
        }

        return true;
    }

    public void printMachineCode() {
        for (String str : machineStatements) {
            System.out.println(str);
        }
    }
}
