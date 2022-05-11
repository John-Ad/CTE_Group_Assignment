package cte_compiler.syntax_analyzer;

public class Node {
    public String value;
    public Node prev;

    public Node() {
        this.value = "";
        this.prev = null;
    }

    public Node(String value, Node prev) {
        this.value = value;
        this.prev = prev;
    }

    public void print(StringBuilder buffer, String prefix, String childrenPrefix) {
        buffer.append(prefix);
        buffer.append(this.value);
        buffer.append('\n');
    }

}
