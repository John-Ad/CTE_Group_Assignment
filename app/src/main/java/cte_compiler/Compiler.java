package cte_compiler;

import java.util.ArrayList;
import java.util.HashMap;

import cte_compiler.code_generation.AssemblyGenerator;
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
 * @author John Adriaans
 * @version 0.1.0
 * @since 2022-04-10
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
        this.symbols.put("=", TOKEN_TYPES.SYMBOL.name());
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

        // ---- STAGE 1: read input and create tokens ----

        Tokenizer tokenizer = new Tokenizer(this.operators, this.symbols);
        tokenizer.readInput("Enter statement: "); // use DONE as value to end input
        System.out.println(tokenizer.getUserInput());

        tokenizer.createTokens();
        ArrayList<Token> tokens = tokenizer.getTokens();
        for (Token t : tokens) {
            System.out.println(t.type + ": " + t.value);
        }

        // ---- STAGE 2: convert tokens into parse tree ----

        ParseTreeGenerator parseTreeGenerator = new ParseTreeGenerator(tokens);
        parseTreeGenerator.generate();
        NonTerminalNode parseTreeRoot = parseTreeGenerator.getTree();
        parseTreeGenerator.printTree(parseTreeRoot);
        System.out.println("Number of nodes: " + parseTreeGenerator.countNodes(parseTreeRoot));

        // ---- STAGE 3: convert parse tree into syntax tree ----
        SyntaxTreeGenerator syntaxTreeGenerator = new SyntaxTreeGenerator();
        syntaxTreeGenerator.generate(parseTreeRoot);
        syntaxTreeGenerator.printInorder(syntaxTreeGenerator.getRoot());
        System.out.println();

        // ---- STAGE 4: convert syntax tree to three address codes ----
        IcrGenerator icrGenerator = new IcrGenerator();
        icrGenerator.generate(syntaxTreeGenerator.getRoot());
        icrGenerator.printIcr();

        // ---- STAGE 5: convert three address codes to assembly statements ----
        AssemblyGenerator assemblyGenerator = new AssemblyGenerator();
        assemblyGenerator.generate(icrGenerator.getReferenceTable(), icrGenerator.getReferenceQueue());
        assemblyGenerator.printAssemblyStatements();

        return null;
    }

}
