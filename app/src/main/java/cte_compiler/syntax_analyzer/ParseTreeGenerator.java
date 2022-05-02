package cte_compiler.syntax_analyzer;

import java.util.ArrayList;
import java.util.Queue;

import cte_compiler.tokenizer.TOKEN_TYPES;
import cte_compiler.tokenizer.Token;

/*
 * 
 *          LANGUAGE GRAMMAR:
 * 
 *    expr -> term + expr | term - expr | term
 *    term -> number * term | number / term | number
 * 
 *    number -> digit{ digit }
 *    digit -> ( "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7"| "8" | "9" )
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

        // check expression
        expression(this.treeRoot);
    }

    // ---- EXPRESSION NON TERMINAL ----
    private void expression(NonTerminalNode parent) {

        // add expression non-terminal
        NonTerminalNode expr = new NonTerminalNode(NON_TERMINAL_TYPES.EXPRESSION.name(), parent);
        parent.children.add(expr);

        // check if there are still tokens left
        if (currenTokenIndex >= tokens.size())
            return;

        Token token = tokens.get(currenTokenIndex);

        // add term
        term(expr);

        // return if there are no tokens
        if (currenTokenIndex >= tokens.size())
            return;

        // get next token
        token = tokens.get(currenTokenIndex);

        // if token is minus/plus op, add op terminal and add next expr
        if (token.value.equals("+") || token.value.equals("-")) {
            expr.children.add(new TerminalNode(token.value, expr, token));
            currenTokenIndex++;

            expression(expr);
        }
    }

    // ---- TERM NON TERMINAL ----
    private void term(NonTerminalNode parent) {

        // add term non-terminal
        NonTerminalNode term = new NonTerminalNode(NON_TERMINAL_TYPES.TERM.name(), parent);
        parent.children.add(term);

        // check if there are still tokens left
        if (currenTokenIndex >= tokens.size())
            return;

        // get next token
        Token token = tokens.get(currenTokenIndex);

        // add number
        number(term);

        // return if there are no tokens
        if (currenTokenIndex >= tokens.size())
            return;

        // get next token
        token = tokens.get(currenTokenIndex);

        // if token is mult/div op, add op terminal and add next term
        if (token.value.equals("*") || token.value.equals("/")) {
            term.children.add(new TerminalNode(token.value, term, token));
            currenTokenIndex++;

            term(term);
        }
    }

    // ---- TERM NON TERMINAL ----
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

    /**
     * Print Tree
     * 
     * prints all nodes in a parse tree.
     * uses breadth first approach using a stack
     * 
     * @param root
     *             root node to expand on
     */
    public void printTree(Node root) {
        ArrayList<Node> queue = new ArrayList<Node>();
        queue.add(root);

        while (!queue.isEmpty()) {

            // get first node in queue
            Node currentNode = queue.remove(0);

            // print node value
            System.out.println(currentNode.value);

            // add children
            if (currentNode instanceof NonTerminalNode) {
                NonTerminalNode ntNode = (NonTerminalNode) currentNode;
                for (Node n : ntNode.children) {
                    queue.add(n);
                }
            }

        }
    }

    /**
     * Count Nodes
     * 
     * counts all nodes in tree.
     * recursively counts each node and its children
     * 
     * @param node
     *             node to count and expand on
     */
    public int countNodes(Node node) {
        int count = 1;

        if (node instanceof NonTerminalNode) {
            NonTerminalNode ntNode = (NonTerminalNode) node;
            for (Node n : ntNode.children) {
                count += countNodes(n);
            }
        }

        return count;
    }

}
