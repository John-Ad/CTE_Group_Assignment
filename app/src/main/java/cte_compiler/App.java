/*
 * 
 *          LANGUAGE GRAMMAR:
 *
 * 
 *    expr -> term + expr | term - expr | term
 *    term -> term * number | term / number | number
 *    
 *    number -> digit{ digit }
 *    digit -> ( "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7"| "8" | "9" )* 
 *
 * 
 *     Test case:
 *     9+8/4-2 
 *     10*2+8
 *     
 *     9+7*5
 *     7-3/2*5+4
 *     
 *     4+6*3+8-2
 *     6+5/3+2
 *     
 *     6++8**2;5
 *     abcdefg
 *     
 *     empty string
 *     8
 * 
 * 
 * 
 */
package cte_compiler;

public class App {
    public static void main(String[] args) {
        Compiler compiler = new Compiler();
        compiler.compile();
    }
}
