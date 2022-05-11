package cte_compiler;

import java.util.ArrayList;
import java.util.HashMap;

import cte_compiler.code_generation.AssemblyGenerator;
import cte_compiler.code_generation.MachineCodeGenerator;
import cte_compiler.icr_generation.IcrGenerator;
import cte_compiler.syntax_analyzer.NonTerminalNode;
import cte_compiler.syntax_analyzer.ParseTreeGenerator;
import cte_compiler.syntax_analyzer.SyntaxTreeGenerator;
import cte_compiler.tokenizer.TOKEN_TYPES;
import cte_compiler.tokenizer.Token;
import cte_compiler.tokenizer.Tokenizer;

/**
 * Links all the various compiler components together. It
 * initializes the keywords, symbols, operators, and comparators
 * based on the language grammar definition.
 * 
 */

public class Compiler {

    private HashMap<String, String> operators;
    private HashMap<String, String> symbols;

    // ---- CONSTRUCTOR ----

    public Compiler() {

        // ---- create empty map objects ----
        this.operators = new HashMap<>();
        this.symbols = new HashMap<>();

        // ---- initialize operators map ----
        this.operators.put("*", TOKEN_TYPES.OPERATOR.name());
        this.operators.put("/", TOKEN_TYPES.OPERATOR.name());
        this.operators.put("-", TOKEN_TYPES.OPERATOR.name());
        this.operators.put("+", TOKEN_TYPES.OPERATOR.name());

        // ---- initialize symbols map ----
        // this.symbols.put("=", TOKEN_TYPES.SYMBOL.name());
        this.symbols.put(";", TOKEN_TYPES.SYMBOL.name());
    }

    // -------------------------------------------------------------------------------
    /**
     * COMPILE
     * 
     * Steps through all the compiler stages to convert a source program gotten from
     * a user to the output language
     * 
     * @return ArrayList<String> --> Line by line array of strings in output
     *         language
     */
    // -------------------------------------------------------------------------------
    public ArrayList<String> compile() {

        int expCount = 1;

        while (true) {

            String prompt = "ENTER NEXT STRING === #" + Integer.toString(expCount)
                    + "\n-Every String/line must end with a semicolon (;)"
                    + "\n-Enter String (Containing 0 to 9 and/or operators: +,/,*,-)"
                    + "\n-Enter EXIT or exit to stop program"
                    + "\nCheck your entering!";

            expCount++;

            // ---- STAGE 1: read input and create tokens ----
            System.out.println("======STAGE1: COMPILER TECHNIQUES--> LEXICAL ANALYSIS-Scanner");
            System.out.println("SYMBOL TABLE COMPRISING ATTRIBUTES AND TOKENS:");

            Tokenizer tokenizer = new Tokenizer(this.operators, this.symbols);
            boolean tokensRead = tokenizer.readInput(prompt); // empty string to exit

            if (!tokensRead)
                break;

            ArrayList<Token> tokens = null;

            if (tokenizer.createTokens()) {
                System.out.println("Successfully created tokens!");
                tokens = tokenizer.getTokens();
                tokenizer.printTokens();
            } else {
                System.out.println("\nFailed to create tokens! Errors found:\n");
                tokenizer.printErrorMessages();
                System.out.println("\n");

                continue; // continue to next iteration
            }

            // ---- STAGE 2: convert tokens into parse tree ----
            System.out.println("======STAGE2: COMPILER TECHNIQUES--> SYNTAX ANALYSIS-Parser");
            System.out.println("GIVEN THE GRAMMAR:\n" + "\t\t\texpr -> term + expr | term - expr | term\n"
                    + "\t\t\tterm -> term * number | term / number | number\n" + "\t\t\tnumber -> digit{ digit }\n"
                    + "\t\t\tdigit -> ( 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7| 8 | 9 )*");
            System.out.println("GET A DERIVATION FOR :  " + tokenizer.getUserInput() + "\n");

            ParseTreeGenerator parseTreeGenerator = new ParseTreeGenerator(tokens);
            parseTreeGenerator.generate();

            if (parseTreeGenerator.getErrorMessages().size() > 0) {
                System.out.println("Expression is is invalid!!");
                parseTreeGenerator.printErrorMessages();
                continue; // continue to next iteration
            }
            System.out.println("Successfully created parse tree! \n Expression is valid.");

            NonTerminalNode parseTreeRoot = parseTreeGenerator.getTree();
            parseTreeGenerator.printTree(parseTreeRoot);
            System.out.println("Number of nodes in parse tree: " + parseTreeGenerator.countNodes(parseTreeRoot));

            // ---- STAGE 3: convert parse tree into syntax tree ----
            System.out.println("======STAGE3: COMPILER TECHNIQUES--> SEMANTIC ANALYSIS");

            SyntaxTreeGenerator syntaxTreeGenerator = new SyntaxTreeGenerator();
            syntaxTreeGenerator.generate(parseTreeRoot);
            syntaxTreeGenerator.printTree(syntaxTreeGenerator.getRoot());
            System.out.println();
            System.out.println("CONCLUSION-->This expression: " + tokenizer.getUserInput()
                    + "is Syntactically and Sematically correct");

            // ---- STAGE 4: convert syntax tree to three address codes ----
            System.out.println("======STAGE4: COMPILER TECHNIQUES--> INTERMEDIATE CODE REPRESENTATION (ICR)");
            System.out.println("THE STRING  ENTERED IS : " + tokenizer.getUserInput());
            System.out.println("The ICR is as follows:\n");

            IcrGenerator icrGenerator = new IcrGenerator();
            icrGenerator.generate(syntaxTreeGenerator.getRoot());
            icrGenerator.printIcr();

            System.out.println("CONCLUSION-->The expression was correctly generated in ICR");

            // ---- STAGE 5: convert three address codes to assembly statements ----
            System.out.println("\n======STAGE5: CODE GENERATION");
            System.out.println("x64 assembly used:\n");

            AssemblyGenerator assemblyGenerator = new AssemblyGenerator();
            assemblyGenerator.generate(icrGenerator.getReferenceTable(), icrGenerator.getReferenceQueue());
            // assemblyGenerator.printAssemblyStatements();
            assemblyGenerator.printAssemblyStatements();

            // ---- STAGE 6: optimise assembly code ----
            System.out.println("\n======STAGE6: CODE OPTIMISATION");
            assemblyGenerator.optimise();
            assemblyGenerator.printAssemblyStatements();

            // ---- STAGE 7: convert assembly into machine code ----
            System.out.println("\n======STAGE7: TARGET MACHINE CODE");
            MachineCodeGenerator machineCodeGenerator = new MachineCodeGenerator();
            boolean mcGenerated = machineCodeGenerator.generateMachineCode(assemblyGenerator.getAssemblyStatements());
            if (mcGenerated) {
                machineCodeGenerator.printMachineCode();
            }

            // ---- END OF COMPILATION ----
            System.out.println(
                    "======END OF COMPILATION\n======THE ORIGINAL INPUT STRING IS:  " + tokenizer.getUserInput()
                            + "\n\n");
        }

        return null;
    }

}
