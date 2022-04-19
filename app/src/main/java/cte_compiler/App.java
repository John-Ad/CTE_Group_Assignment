/*
 * 
 *          LANGUAGE GRAMMAR:
 * 
 *    prog ::= {stmt}
 *    stmt ::= "print" (number | id | string) ;
 *        |    "var" id "=" (expr | string) ;
 *        |    id = expr ;
 *        |    "while" comp "do" {stmt} "end" ;
 *        |    "if" comp "then" {stmt} "end" 
 *    comp ::= expr (comp_op expr)
 *    comp_op ::= ( "==" | "<" | ">" | ">=" | "<=" | "!=")
 *    expr ::= term {("+" | "-") term}
 *    term ::= primary {( "/" | "*" ) primary}
 *    primary ::= digit{ digit }
 *    digit ::= ( "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7"| "8" | "9" )
 *    id ::= string
 * 
 */
package cte_compiler;

public class App {
    public static void main(String[] args) {
        Compiler compiler = new Compiler();
        compiler.compile();
    }
}
