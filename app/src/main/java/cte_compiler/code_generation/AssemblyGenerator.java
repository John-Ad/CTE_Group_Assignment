package cte_compiler.code_generation;

import java.util.ArrayList;
import java.util.Hashtable;

import cte_compiler.grammar_enums.OPERATORS;
import cte_compiler.icr_generation.ReferenceTable;
import cte_compiler.icr_generation.TACArg;
import cte_compiler.icr_generation.TAC_ARG_TYPES;
import cte_compiler.icr_generation.ThreeAddressCode;

/**
 * TO DO:
 * 
 * implement register pool manager
 * optimise generated code by performing calculation and MOV result into eax
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

                // perform mul or div
                AssemblyStatement st2 = new AssemblyStatement(ASSEMBLY_KEYWORDS.getKeywordForOp(op),
                        new AssemblyArg(arg2Val, arg2Type),
                        null);

                // store in new or old register
                AssemblyStatement st3 = new AssemblyStatement(ASSEMBLY_KEYWORDS.MOV,
                        new AssemblyArg(register, ASSEMBLY_ARG_TYPES.REGISTER),
                        new AssemblyArg("eax", ASSEMBLY_ARG_TYPES.REGISTER));

                // add statements

                this.assemblyStatementQueue.add(st1);
                this.assemblyStatementQueue.add(st2);
                this.assemblyStatementQueue.add(st3);

                // map ref to register
                refToRegMap.put(refName, register);

                // continue to next iter
                continue;
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
                 * or t3 = t1 / 10 -> div eax, 10 // no mov op needed
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
    }

    public void printAssemblyStatements() {
        for (AssemblyStatement statement : this.assemblyStatementQueue) {

            System.out.print(statement.instruction.toString() + " " + statement.arg1.value);
            if (statement.arg2 != null) {
                System.out.print(", " + statement.arg2.value);
            }

            System.out.println();
        }
    }
}
