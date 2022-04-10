package cte_compiler;

import java.util.ArrayList;
import java.util.HashMap;

import cte_compiler.tokenizer.TOKEN_TYPES;

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

    private HashMap<String, String> keywords;
    private HashMap<String, String> operators;
    private HashMap<String, String> symbols;
    private HashMap<String, String> comparators;

    // ---- CONSTRUCTOR ----

    public Compiler() {

        // ---- create empty map objects ----
        this.keywords = new HashMap<>();
        this.operators = new HashMap<>();
        this.symbols = new HashMap<>();
        this.comparators = new HashMap<>();

        // ---- initialize kewords map ----
        this.keywords.put("var", TOKEN_TYPES.KEYWORD.name());
        this.keywords.put("print", TOKEN_TYPES.KEYWORD.name());
        this.keywords.put("if", TOKEN_TYPES.KEYWORD.name());
        this.keywords.put("then", TOKEN_TYPES.KEYWORD.name());
        this.keywords.put("while", TOKEN_TYPES.KEYWORD.name());
        this.keywords.put("do", TOKEN_TYPES.KEYWORD.name());
        this.keywords.put("end", TOKEN_TYPES.KEYWORD.name());

        // ---- initialize operators map ----
        this.operators.put("*", TOKEN_TYPES.OPERATOR.name());
        this.operators.put("/", TOKEN_TYPES.OPERATOR.name());
        this.operators.put("-", TOKEN_TYPES.OPERATOR.name());
        this.operators.put("+", TOKEN_TYPES.OPERATOR.name());

        // ---- initialize symbols map ----
        this.symbols.put("=", TOKEN_TYPES.SYMBOL.name());

        // ---- initialize comparators map ----
        this.comparators.put("==", TOKEN_TYPES.COMPARATOR.name());
        this.comparators.put("<=", TOKEN_TYPES.COMPARATOR.name());
        this.comparators.put(">=", TOKEN_TYPES.COMPARATOR.name());
        this.comparators.put("!=", TOKEN_TYPES.COMPARATOR.name());
        this.comparators.put(">", TOKEN_TYPES.COMPARATOR.name());
        this.comparators.put("<", TOKEN_TYPES.COMPARATOR.name());
    }

    /**
     * Steps through all the compiler stages to convert a source program gotten from
     * a user to the output language
     * 
     * @return ArrayList<String> --> Line by line array of strings in output
     *         language
     */
    public ArrayList<String> compile() {

        // ---- STAGE 1: read input and create tokens ----

        return null;
    }

}
