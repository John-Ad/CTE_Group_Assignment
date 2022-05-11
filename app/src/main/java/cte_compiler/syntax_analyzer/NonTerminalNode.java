package cte_compiler.syntax_analyzer;

import java.io.NotActiveException;
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

    public void printTree() {
        StringBuilder buffer = new StringBuilder(50);
        print(buffer, "", "");
        System.out.println(buffer.toString());
    }

    public void print(StringBuilder buffer, String prefix, String childrenPrefix) {
        buffer.append(prefix);
        buffer.append(this.value);
        buffer.append('\n');
        for (int i = this.children.size() - 1; i >= 0; i--) {
            Node n = this.children.get(i);
            if (n instanceof NonTerminalNode) {
                NonTerminalNode nt = (NonTerminalNode) n;
                nt.print(buffer, childrenPrefix + "├── ", childrenPrefix + "│   ");
            } else {
                n.print(buffer, childrenPrefix + "└── ", childrenPrefix + "    ");
            }
        }
    }
}
