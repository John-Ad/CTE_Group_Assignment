package cte_compiler.syntax_analyzer;

import java.util.ArrayList;

public class SyntaxTreeGenerator {
    private SyntaxNode root;

    public SyntaxTreeGenerator() {
        this.root = null;
    }

    public void generate(NonTerminalNode parseTreeRoot) {
        this.root = parse(parseTreeRoot);
    }

    /**
     * Parse
     * 
     * recursively checks all children of non terminals and tries to find terminal
     * nodes. These terminal nodes are then used to construct a syntax tree.
     * Based on the grammar of our language, each non terminal will either contain 3
     * children consisting of 2 non terminals and 1 terminal operator, or it will
     * have only 1 child which will be either a terminal number or non terminal.
     * 
     * if there are 3 children, the middle child is an operator and will form the
     * parent for all nodes that follow. This parent will be returned. The 2 other
     * children will be expanded upon recursively and will form the left and right
     * children of the parent node which will be returned.
     * 
     * if there is only 1 child and it is a terminal, the child will be a number.
     * This number will be returned.
     * else, the child is a non terminal and will be expanded upon in a recursive
     * call to parse()
     * 
     * @param node
     *             node whose children will be checked and expanded upon
     */
    private SyntaxNode parse(NonTerminalNode node) {

        // non terminal nodes contain either 1 child or 3, nothing more or less

        SyntaxNode newParent = null;

        // if size of children > 1, then there are operators present
        if (node.children.size() == 3) {

            // create syntax node that children of this nonterminal will be children to
            TerminalNode tNode = (TerminalNode) node.children.get(1);
            newParent = new SyntaxNode(tNode.value, null, tNode.token);
        }

        // loop through children
        for (int i = 0; i < node.children.size(); i++) {

            // parse non terminal children for terminals
            if (node.children.get(i) instanceof NonTerminalNode) {

                NonTerminalNode ntNode = (NonTerminalNode) node.children.get(i);

                SyntaxNode tempNode = parse(ntNode);

                // add node to newParent if it isnt null
                if (newParent != null) { // if new parent isnt null, it means there are 3 children with the middle one
                                         // being an operator

                    tempNode.prev = newParent;

                    if (newParent.left == null)
                        newParent.left = tempNode;
                    else if (newParent.right == null)
                        newParent.right = tempNode;
                } else { // there is only 1 child so return syntax node
                    return tempNode;
                }
            }

            // parse terminals
            if (node.children.get(i) instanceof TerminalNode) {

                // if there is only 1 child, return syntax node of this terminal
                if (node.children.size() == 1) {
                    TerminalNode tNode = (TerminalNode) node.children.get(i);
                    return new SyntaxNode(tNode.value, null, tNode.token);
                }
            }
        }

        // if this point is reached, there were 3 children, meaning newParent was
        // initialized. If it is still null at this point, and error occured in the
        // program or an invalid parse tree was provided
        return newParent;
    }

    public SyntaxNode getRoot() {
        return this.root;
    }

    /**
     * Print Inorder
     * 
     * recursively prints all node values in a syntax tree.
     * The order is: left -> node -> right
     * 
     * eg
     * ......... + ........
     * ........ /.\ .......
     * ....... 3...4 ......
     * 
     * prints: 3 + 4
     * 
     * @param node
     *             node to expand on or print depending on its type
     */
    public void printInorder(SyntaxNode node) {
        if (node == null)
            return;

        printInorder(node.left);
        System.out.print(node.value + " ");
        printInorder(node.right);
    }

    /**
     * Count Nodes
     * 
     * recursively counts each node and its children
     * 
     * @param node
     *             node to count and expand on
     */
    public int countNodes(SyntaxNode node) {
        if (node == null)
            return 0;

        return 1 + countNodes(node.left) + countNodes(node.right);
    }

}
