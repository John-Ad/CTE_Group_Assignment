package cte_compiler.syntax_analyzer;

import java.util.ArrayList;

public class NonTerminalNode extends Node {
    public ArrayList<Node> children;

    public NonTerminalNode() {
        super();

        this.children = new ArrayList<>();
    }

    public NonTerminalNode(String value, Node prev) {
        super(value, prev);

        this.children = new ArrayList<>();
    }
}
