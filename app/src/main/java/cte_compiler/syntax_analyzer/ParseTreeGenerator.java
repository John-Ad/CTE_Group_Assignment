package cte_compiler.syntax_analyzer;

import java.util.ArrayList;

import cte_compiler.grammar_enums.SYMBOLS;
import cte_compiler.tokenizer.TOKEN_TYPES;
import cte_compiler.tokenizer.Token;

/*
 * 
 *          LANGUAGE GRAMMAR:
 * 
 *    expr ::= term {("+" | "-") term}
 *    term ::= num {( "/" | "*" ) num}
 *    num ::= digit{ digit }
 *    digit ::= ( "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7"| "8" | "9" )
 * 
 */

public class ParseTreeGenerator {
    private NonTerminalNode treeRoot;
    private ArrayList<Token> tokens;

    private int currenTokenIndex; // tracks current token

    // ---- CONSTRUCTOR ----
    public ParseTreeGenerator(ArrayList<Token> tokens) {
        this.treeRoot = new NonTerminalNode(NON_TERMINAL_TYPES.PROGRAM.name(), null);

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
    }

    // ---- EXPRESSION NON TERMINAL ----
    private void expression(NonTerminalNode parent) {

        // add expression non-terminal
        NonTerminalNode expr = new NonTerminalNode(NON_TERMINAL_TYPES.EXPRESSION.name(), parent);
        parent.children.add(expr);

        // add first term
        term(expr);

        if (currenTokenIndex >= tokens.size())
            return;

        Token token = tokens.get(currenTokenIndex);

        // add rest of expression
        while (token.value.equals("-") || token.value.equals("+")) {
            // add operator
            expr.children.add(new TerminalNode(token.value, expr, token));

            // advance to next token
            currenTokenIndex++;

            // add term
            term(expr);

            if (currenTokenIndex >= tokens.size()) {
                break;
            }

            token = tokens.get(currenTokenIndex);
        }
    }

    private void term(NonTerminalNode parent) {

        // add term non-terminal
        NonTerminalNode term = new NonTerminalNode(NON_TERMINAL_TYPES.TERM.name(), parent);
        parent.children.add(term);

        // add first primary
        // primary(term);

        if (currenTokenIndex >= tokens.size())
            return;

        Token token = tokens.get(currenTokenIndex);

        // add rest of expression
        while (token.value.equals("*") || token.value.equals("/")) {
            // add operator
            term.children.add(new TerminalNode(token.value, term, token));

            // advance to next token
            currenTokenIndex++;

            // add term
            // primary(term);

            if (currenTokenIndex >= tokens.size()) {
                break;
            }

            token = tokens.get(currenTokenIndex);
        }
    }

    private void number(NonTerminalNode parent) {

        // add number non-terminal
        NonTerminalNode num = new NonTerminalNode(NON_TERMINAL_TYPES.NUMBER.name(), parent);
        parent.children.add(num);

        Token token = tokens.get(currenTokenIndex);

        // add number
        num.children.add(new TerminalNode(token.value, num, token));

        // move to next token
        currenTokenIndex++;
    }

    public NonTerminalNode getTree() {
        return this.treeRoot;
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
