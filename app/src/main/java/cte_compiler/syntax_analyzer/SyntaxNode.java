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
}
