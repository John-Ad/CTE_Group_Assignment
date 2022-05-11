package cte_compiler.code_generation;

import java.util.ArrayList;
import java.util.Hashtable;

import cte_compiler.grammar_enums.OPERATORS;
import cte_compiler.icr_generation.ReferenceTable;
import cte_compiler.icr_generation.TACArg;
import cte_compiler.icr_generation.TAC_ARG_TYPES;
import cte_compiler.icr_generation.ThreeAddressCode;

/**
 * ASSEMBLY GENERATOR CLASS
 *
 * Uses x64 instruction set, but only uses 32bit registers.
 * 
 * Converts three address code into assembly instructions.
 * 
 * x64 operations used:
 * - mov --> moves data into a register, 2 operands
 * - add --> performs addition, 2 operands
 * - sub --> performs subtraction, 2 operands
 * - div --> performs division on whatever value is stored in register eax, 1
 * operand
 * - imul --> either performs multiplication on value in eax, or performs
 * multiplication on one register and stores it in another, 1 operand (normal
 * mul), 2 operands, or 3 operands (stores result in first operand)
 * 
 * 
 */

public class AssemblyGenerator {
    private Hashtable<String, String> refToRegMap;
    private RegisterPool registerPool;

    private ArrayList<AssemblyStatement> assemblyStatementQueue;

    public AssemblyGenerator() {
        this.refToRegMap = new Hashtable<String, String>();
        this.registerPool = new RegisterPool();

        this.assemblyStatementQueue = new ArrayList<AssemblyStatement>();
    }

    public void generate(ReferenceTable refTable, ArrayList<String> refQueue) {
        for (String refName : refQueue) {
            ThreeAddressCode tac = refTable.getRef(refName);

            // get args
            TACArg arg1 = tac.getFirstArg();
            TACArg arg2 = tac.getSecondArg();

            // get op
            OPERATORS op = tac.getOperator();

            String register = "";

            /*
             * Set register
             * 
             * if arg1 a ref, reuse register for that ref
             * 
             * e.g t1 = 4 + 5 -> mov eax, 4
             * ----------------- add eax, 5
             * or t3 = t1 / 10 -> div eax, 10 // no mov op needed
             * 
             * in the case above, eax will be reused instead of a new register
             * being used
             * 
             */
            if (arg1.type == TAC_ARG_TYPES.REFERENCE) {
                register = refToRegMap.get(arg1.value); // reuse old
            } else {
                register = registerPool.getRegister(); // get new
            }

            /**
             * if op is * or /, then the eax register should be used
             * and the result should be stored in a new register
             */
            if (op == OPERATORS.DIV || op == OPERATORS.MULT) {

                // create first args values
                String arg1Val = arg1.value;
                ASSEMBLY_ARG_TYPES arg1Type = ASSEMBLY_ARG_TYPES.tacArgConversion(arg1.type);

                // get register value of arg2 if applicable
                if (arg1.type == TAC_ARG_TYPES.REFERENCE) {
                    arg1Val = refToRegMap.get(arg1.value);
                }

                // create second args values
                String arg2Val = arg2.value;
                ASSEMBLY_ARG_TYPES arg2Type = ASSEMBLY_ARG_TYPES.tacArgConversion(arg2.type);

                // get register value of arg2 if applicable
                if (arg2.type == TAC_ARG_TYPES.REFERENCE) {
                    arg2Val = refToRegMap.get(arg2.value);
                }

                // move first arg to eax
                AssemblyStatement st1 = new AssemblyStatement(ASSEMBLY_KEYWORDS.MOV,
                        new AssemblyArg("eax", ASSEMBLY_ARG_TYPES.REGISTER),
                        new AssemblyArg(arg1Val, arg1Type));

                // if DIV
                if (op == OPERATORS.DIV) {

                    // move second arg to ebx
                    AssemblyStatement st2 = new AssemblyStatement(ASSEMBLY_KEYWORDS.MOV,
                            new AssemblyArg("ebx", ASSEMBLY_ARG_TYPES.REGISTER),
                            new AssemblyArg(arg2Val, arg2Type));

                    // perform div using ebx
                    AssemblyStatement st3 = new AssemblyStatement(ASSEMBLY_KEYWORDS.DIV,
                            new AssemblyArg("ebx", ASSEMBLY_ARG_TYPES.REGISTER),
                            null);

                    // store in new or old register
                    AssemblyStatement st4 = new AssemblyStatement(ASSEMBLY_KEYWORDS.MOV,
                            new AssemblyArg(register, ASSEMBLY_ARG_TYPES.REGISTER),
                            new AssemblyArg("eax", ASSEMBLY_ARG_TYPES.REGISTER));

                    // add statements
                    this.assemblyStatementQueue.add(st1);
                    this.assemblyStatementQueue.add(st2);
                    this.assemblyStatementQueue.add(st3);
                    this.assemblyStatementQueue.add(st4);
                }

                // if MUL
                if (op == OPERATORS.MULT) {

                    // perform imul using eax
                    AssemblyStatement st2 = new AssemblyStatement(ASSEMBLY_KEYWORDS.MUL,
                            new AssemblyArg("eax", ASSEMBLY_ARG_TYPES.REGISTER),
                            new AssemblyArg(arg2Val, arg2Type));

                    // store in new or old register
                    AssemblyStatement st3 = new AssemblyStatement(ASSEMBLY_KEYWORDS.MOV,
                            new AssemblyArg(register, ASSEMBLY_ARG_TYPES.REGISTER),
                            new AssemblyArg("eax", ASSEMBLY_ARG_TYPES.REGISTER));

                    // add statements
                    this.assemblyStatementQueue.add(st1);
                    this.assemblyStatementQueue.add(st2);
                    this.assemblyStatementQueue.add(st3);
                }

                // add statements

                // map ref to register
                refToRegMap.put(refName, register);

            } else {
                /*
                 * if arg1 is a constant, a MOV operation is needed
                 * 
                 * e.g t1 = 4 + 5 -> mov eax, 4
                 * ----------------- add eax, 5
                 * or t3 = 5 / t1 -> mov ebx, 5
                 * ----------------- div ebx, eax // t1 is stored in eax
                 */
                if (arg1.type == TAC_ARG_TYPES.CONSTANT) {

                    // create second args values
                    String arg2Val = arg2.value;
                    ASSEMBLY_ARG_TYPES arg2Type = ASSEMBLY_ARG_TYPES.tacArgConversion(arg2.type);

                    // get register value of arg2 if applicable
                    if (arg2.type == TAC_ARG_TYPES.REFERENCE) {
                        arg2Val = refToRegMap.get(arg2.value);
                    }

                    // create assembly statements
                    AssemblyStatement statement1 = new AssemblyStatement(ASSEMBLY_KEYWORDS.MOV,
                            new AssemblyArg(register, ASSEMBLY_ARG_TYPES.REGISTER),
                            new AssemblyArg(arg1.value, ASSEMBLY_ARG_TYPES.CONSTANT));

                    AssemblyStatement statement2 = new AssemblyStatement(
                            ASSEMBLY_KEYWORDS.getKeywordForOp(tac.getOperator()),
                            new AssemblyArg(register, ASSEMBLY_ARG_TYPES.REGISTER),
                            new AssemblyArg(arg2Val, arg2Type));

                    // add statements to queue
                    this.assemblyStatementQueue.add(statement1);
                    this.assemblyStatementQueue.add(statement2);

                    // map ref to register
                    refToRegMap.put(refName, register);

                }

                /*
                 * if arg1 is a ref, a MOV operation is NOT needed
                 * because the operation's result can be stored in
                 * arg1's register
                 * 
                 * e.g t1 = 4 + 5 -> mov eax, 4
                 * ----------------- add eax, 5
                 * or t3 = t1 + 5 -> add eax, 5 // no mov op needed
                 */
                if (arg1.type == TAC_ARG_TYPES.REFERENCE) {

                    // create second args values
                    String arg2Val = arg2.value;
                    ASSEMBLY_ARG_TYPES arg2Type = ASSEMBLY_ARG_TYPES.tacArgConversion(arg2.type);

                    // get register value of arg2 if applicable
                    if (arg2.type == TAC_ARG_TYPES.REFERENCE) {
                        arg2Val = refToRegMap.get(arg2.value);
                    }

                    // create assembly statement
                    AssemblyStatement statement = new AssemblyStatement(
                            ASSEMBLY_KEYWORDS.getKeywordForOp(tac.getOperator()),
                            new AssemblyArg(register, ASSEMBLY_ARG_TYPES.REGISTER),
                            new AssemblyArg(arg2Val, arg2Type));

                    // add statement to queue
                    this.assemblyStatementQueue.add(statement);

                    // map ref to register
                    refToRegMap.put(refName, register);
                }

            }

            /**
             * Free registers that are not needed anymore.
             * 
             * Only registers that are form the second arg in a
             * statement can be freed, since the register for the
             * first arg is reused
             */
            if (arg2.type == TAC_ARG_TYPES.REFERENCE) {
                if (refToRegMap.contains(arg2.value)) {
                    String regToFree = this.refToRegMap.remove(arg2.value);
                    registerPool.freeRegister(regToFree);
                }
            }

        }
    }

    /**
     * OPTIMISE
     * 
     * take the generated assembly statements and optimise them.
     * 
     * Optimization rules:
     * 
     * (1). replace statements like: imul eax, 9
     * --------------------------- mov r8d, eax
     * --------------------- with: imul r8d, eax, 9
     * 
     * (2). if 2 mov statements negate each other, eg mov r10d, eax followed by mov
     * eax, r10d remove both move statements since it ends with eax anyway
     * 
     * (3). if a value is moved to a register and that register is then added to
     * another register immediately afterwards, then just add the value to the
     * second register instead.
     * e.g mov r9d, eax
     * --- add r8d, r9d
     * becomes: add r8d, eax
     */
    public void optimise() {

        int i = 0;

        // ----------------------------------------------------------------
        // RULE 2, removal of back to back mov ops that negate each other
        // ----------------------------------------------------------------
        while (i < this.assemblyStatementQueue.size()) {

            // get first statement to compare
            AssemblyStatement statement1 = this.assemblyStatementQueue.get(i);

            // if there are back to back statements to compare
            if (i + 1 < this.assemblyStatementQueue.size()) {

                // get second statement to compare
                AssemblyStatement statement2 = this.assemblyStatementQueue.get(i + 1);

                // if both statements are mov
                if (statement1.instruction == ASSEMBLY_KEYWORDS.MOV
                        && statement2.instruction == ASSEMBLY_KEYWORDS.MOV) {

                    // if s1.arg1 == s2.arg2 and s1.arg2 == s2.arg1
                    if (statement1.arg1.value == statement2.arg2.value
                            && statement1.arg2.value == statement2.arg1.value) {
                        // remove both statements
                        this.assemblyStatementQueue.remove(i + 1);
                        this.assemblyStatementQueue.remove(i);

                        // decrease i by 1
                        i--;
                    }
                }

                // ----------------------------------------------------------------
            }
            i++;
        }

        i = 0;
        // ----------------------------------------------------------
        // RULE 1, conversion of imul to avoid unnecessary mov
        // ----------------------------------------------------------
        while (i < this.assemblyStatementQueue.size()) {

            // get first statement to compare
            AssemblyStatement statement1 = this.assemblyStatementQueue.get(i);

            // if there are back to back statements to compare
            if (i + 1 < this.assemblyStatementQueue.size()) {

                // get second statement to compare
                AssemblyStatement statement2 = this.assemblyStatementQueue.get(i + 1);

                // if s1 == mul op and s2 == mov op
                if (statement1.instruction == ASSEMBLY_KEYWORDS.MUL
                        && statement2.instruction == ASSEMBLY_KEYWORDS.MOV) {

                    // if s1 arg1 reg == s2 arg2 reg
                    if (statement1.arg1.value == statement2.arg2.value) {

                        // create optimised imul
                        AssemblyStatement optMul = new AssemblyStatement(ASSEMBLY_KEYWORDS.MUL, statement2.arg1,
                                statement1.arg1);
                        optMul.arg3 = statement1.arg2;

                        // remove old imul and mov
                        this.assemblyStatementQueue.remove(i + 1);
                        this.assemblyStatementQueue.remove(i);

                        // replace with new optimized imul
                        this.assemblyStatementQueue.add(i, optMul);
                    }
                }
            }

            i++;
        }

        i = 0;
        // ----------------------------------------------------------------
        // RULE 3, removal of unecessary mov before add or sub
        // ----------------------------------------------------------------
        while (i < this.assemblyStatementQueue.size()) {

            // get first statement to compare
            AssemblyStatement statement1 = this.assemblyStatementQueue.get(i);

            // if there are back to back statements to compare
            if (i + 1 < this.assemblyStatementQueue.size()) {

                // get second statement to compare
                AssemblyStatement statement2 = this.assemblyStatementQueue.get(i + 1);

                // if s1 == mul op and s2 == mov op
                if (statement1.instruction == ASSEMBLY_KEYWORDS.MOV
                        && (statement2.instruction == ASSEMBLY_KEYWORDS.ADD
                                || statement2.instruction == ASSEMBLY_KEYWORDS.SUB)) {

                    // if s1.arg1 reg == s2.arg2 reg
                    if (statement1.arg1.value == statement2.arg2.value) {

                        // create optimised add/sub
                        AssemblyStatement optCode = new AssemblyStatement(statement2.instruction, statement2.arg1,
                                statement1.arg2);

                        // remove old code
                        this.assemblyStatementQueue.remove(i + 1);
                        this.assemblyStatementQueue.remove(i);

                        // replace with new optimized code
                        this.assemblyStatementQueue.add(i, optCode);
                    }
                }
            }

            i++;
        }
    }

    public ArrayList<AssemblyStatement> getAssemblyStatements() {
        return this.assemblyStatementQueue;
    }

    public void printAssemblyStatements() {
        for (AssemblyStatement statement : this.assemblyStatementQueue) {

            System.out.print(statement.instruction.toString() + " " + statement.arg1.value);
            if (statement.arg2 != null) {
                System.out.print(", " + statement.arg2.value);
            }
            if (statement.arg3 != null) {
                System.out.print(", " + statement.arg3.value);
            }

            System.out.println();
        }
    }
}
