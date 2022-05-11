package cte_compiler.syntax_analyzer;

import cte_compiler.tokenizer.Token;

public class SyntaxNode extends TerminalNode {
    public SyntaxNode left;
    public SyntaxNode right;

    public SyntaxNode() {
        super();

        this.left = null;
        this.right = null;
    }

    public SyntaxNode(String value, Node prev, Token token) {
        super(value, prev, token);
    }

    public void printTree() {
        System.out.println(this.print(new StringBuilder(), true, new StringBuilder()).toString());
    }

    private StringBuilder print(StringBuilder prefix, boolean isTail, StringBuilder sb) {
        if (right != null) {
            right.print(new StringBuilder().append(prefix).append(isTail ? "│   " : "    "), false, sb);
        }
        sb.append(prefix).append(isTail ? "└── " : "┌── ").append(value.toString()).append("\n");
        if (left != null) {
            left.print(new StringBuilder().append(prefix).append(isTail ? "    " : "│   "), true, sb);
        }
        return sb;
    }

}
