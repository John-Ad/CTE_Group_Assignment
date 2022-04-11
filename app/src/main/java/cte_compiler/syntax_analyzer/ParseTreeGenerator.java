package cte_compiler.syntax_analyzer;

import java.util.ArrayList;

import cte_compiler.grammar_enums.KEYWORDS;
import cte_compiler.tokenizer.TOKEN_TYPES;
import cte_compiler.tokenizer.Token;

/*
 * 
 *          LANGUAGE GRAMMAR:
 * 
 *    prog ::= {stmt}
 *    stmt ::= "print" (expr | string) ;
 *        |    "var" id "=" expr ;
 *        |    id = expr ;
 *        |    "while" comp "do" {stmt} "end" ;
 *        |    "if" comp "then" {stmt} "end" 
 *    comp ::= expr (comp_op expr)
 *    comp_op ::= ( "==" | "<" | ">" | ">=" | "<=" | "!=")
 *    expr ::= term {("+" | "-") term}
 *    term ::= unary {( "/" | "*" ) unary}
 *    unary ::= [ "+" | "-" ] primary
 *    primary ::= num | id
 *    num ::= digit{ digit }
 *    digit ::= ( "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7"| "8" | "9" )
 *    id ::= string
 * 
 */

public class ParseTreeGenerator {
    private NonTerminalNode tree;
    private ArrayList<Token> tokens;

    private int currenTokenIndex; // tracks current token

    // ---- CONSTRUCTOR ----
    public ParseTreeGenerator(ArrayList<Token> tokens) {
        this.tree = new NonTerminalNode(NON_TERMINAL_TYPES.PROGRAM.name(), null);

        this.tokens = tokens;
        this.currenTokenIndex = 0;
    }

    /**
     * GENERATE
     * 
     * start parse tree generation process
     */
    public void generate() {

        // return if there are no tokens
        if (this.tokens.size() == 0) {
            return;
        }

        // loop until all statements have been checked
        while (true) {
            Token token = tokens.get(currenTokenIndex);

            // add statement node
            if (token.type == TOKEN_TYPES.KEYWORD || token.type == TOKEN_TYPES.IDENTIFIER) {
                statement(this.tree);
            } else {
                break; // exit loop if one of the statements not matched
            }

            currenTokenIndex++;

            // exit loop if no tokens are left
            if (currenTokenIndex >= tokens.size())
                break;
        }

    }

    // --------------------------------------
    //
    // STATEMENT GRAMMAR FUNCTIONS
    //
    // --------------------------------------

    // ---- ROOT STATEMENT ----

    private void statement(NonTerminalNode parent) {

        // add statement node to tree
        NonTerminalNode stmt = new NonTerminalNode(NON_TERMINAL_TYPES.STATEMENT.toString(), parent);
        parent.children.add(stmt);

        Token token = tokens.get(currenTokenIndex);

        // decide which branch to continue down

        // var statement
        if (token.value == KEYWORDS.VAR.toString())
            declarationStatement(stmt);

        // print statement
        if (token.value == KEYWORDS.PRINT.toString())
            printStatement();
    }

    // ---- DECLARATION STATEMENT ----

    private void declarationStatement(NonTerminalNode parent) {
        // add var terminal keyword to tree
        parent.children.add(new TerminalNode(KEYWORDS.VAR.toString(), parent, tokens.get(currenTokenIndex)));

        // advance to next token
        currenTokenIndex++;

        // add identifier to tree
        identifier(parent);
    }

    private void ifStatement() {
    }

    private void whileStatement() {
    }

    private void assignmentStatement() {
    }

    private void printStatement() {
    }

    // -------------------------------------

    private void expression() {
    }

    // ---- IDENITFIER NON TERMINAL ----
    private void identifier(NonTerminalNode parent) {

        Token token = tokens.get(currenTokenIndex);

        // create id non terminal
        NonTerminalNode id = new NonTerminalNode(NON_TERMINAL_TYPES.IDENTIFIER.name(), parent);

        // add terminal as child to id
        id.children.add(new TerminalNode(token.value, parent, token));

        // add id to tree
        parent.children.add(new NonTerminalNode(NON_TERMINAL_TYPES.IDENTIFIER.name(), parent));

    }

    private void comparison() {
    }

    private void term() {
    }

    private void primary() {
    }

    private void number() {
    }

    private void digit() {
    }

    public NonTerminalNode getTree() {
        return this.tree;
    }

    /**
     * Print Terminals
     * 
     * prints all terminal values in a parse tree.
     * recursively goes through tree using in-order traversal
     * 
     * @param node
     *             node to expand on or print depending on its type
     */
    public void printTerminals(Node node) {
        /**
         * if non terminal, go through all children
         * in order from left to right
         */
        if (node instanceof NonTerminalNode) {
            NonTerminalNode ntNode = (NonTerminalNode) node;
            for (Node n : ntNode.children) {
                printTerminals(n);
            }
        }

        /**
         * if terminal, print value
         */
        if (node instanceof TerminalNode) {
            TerminalNode tNode = (TerminalNode) node;
            System.out.println(tNode.value + " ");
        }
    }
}
