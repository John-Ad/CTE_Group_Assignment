package cte_compiler.syntax_analyzer;

import java.util.ArrayList;

import cte_compiler.tokenizer.Token;

/**
 * ParseTreeGenerator
 * 
 * @author John Adriaans
 * @version 0.1.0
 */
public class TerminalNode extends Node {
    public Token token;

    public TerminalNode() {
        super();

        token = null;
    }

    public TerminalNode(String value, Node prev, Token token) {
        super(value, prev);

        this.token = token;
    }
}