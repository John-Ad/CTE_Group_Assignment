package cte_compiler.tokenizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class Tokenizer {
    private Scanner scanner;
    private String userInput;
    private int currentIndex;

    private HashMap<String, String> operators;
    private ArrayList<Token> tokens;
    private HashMap<String, String> bracesBracketsEtc;
    private HashMap<String, String> keywords;
    private ArrayList<Token> allTokens;

    public Tokenizer(ArrayList<String> operators) {
        this.scanner = new Scanner(System.in);
        this.userInput = "";
        this.operators = new HashMap<>();
        this.tokens = new ArrayList<>();
        this.currentIndex = 0;

        allTokens = new ArrayList<>();
        keywords = new HashMap<String, String>();
        bracesBracketsEtc = new HashMap<String, String>();

        // initialize keywords
        keywords.put("int", "int");
        keywords.put("char", "char");
        keywords.put("float", "float");
        keywords.put("double", "double");
        keywords.put("printf", "printf");
        keywords.put("Scanf", "Scanf");

        // initialize braces etc
        bracesBracketsEtc.put("{", "OpenBrace");
        bracesBracketsEtc.put("[", "OpenBracket");
        bracesBracketsEtc.put("(", "OpenParanthesis");
        bracesBracketsEtc.put("}", "CloseBrace");
        bracesBracketsEtc.put("]", "CloseBracket");
        bracesBracketsEtc.put(")", "CloseParanthesis");

        // initialize reserved strings with token identifiers
        for (String op : operators) {
            this.operators.put(op, op);
            this.operators.put(op, op);
        }
    }

    /**
     * ------------------------------------------------
     * CREATE TOKENS
     * 
     * generates tokens for user input
     * ------------------------------------------------
     */
    public void createTokens() {
        int stringCounter = 1; // track string number

        System.out.println("Enter number of lines:");
        int lines = Integer.parseInt(scanner.nextLine());

        while (stringCounter <= lines) {

            // read line of input
            readInput("Enter String #" + Integer.toString(stringCounter) + ":");

            // continue until user enters "Exit"
            if (userInput.equals("Exit")) {
                return;
            }

            if (userInput.length() == 0) {
                return;
            }

            while (true) {
                if (currentIndex >= userInput.length()) {
                    break;
                }

                // skip whitespace
                skipWhiteSpace();

                // check for symbols
                if (userInput.charAt(currentIndex) == '=') {
                    tokens.add(new Token("Symbol", Character.toString(userInput.charAt(currentIndex))));
                }

                // check for operators
                if (operators.containsKey(Character.toString(userInput.charAt(currentIndex)))) {
                    tokens.add(new Token("operator",
                            Character.toString(userInput.charAt(currentIndex))));
                }

                // check for braces
                if (bracesBracketsEtc.containsKey(Character.toString(userInput.charAt(currentIndex)))) {
                    tokens.add(new Token(bracesBracketsEtc.get(Character.toString(userInput.charAt(currentIndex))),
                            Character.toString(userInput.charAt(currentIndex))));
                }

                // check for words
                if (Character.isAlphabetic(userInput.charAt(currentIndex))) {
                    parseIdentifier();
                    continue; // move to next iteration of loop
                }

                // check for numbers
                /**
                 * TO BE IMPLEMENTED
                 */

                // move to next char
                currentIndex++;
            }

            // print tokens
            printTokens();

            System.out.println("TOTAL NUMBER OF TOKENS FOR STRING #" + Integer.toString(stringCounter) + " : "
                    + Integer.toString(tokens.size()));
            System.out.println("END OF STRING #" + Integer.toString(stringCounter));
            System.out.println("=====================================\n");

            // add tokens to allTokens
            allTokens.addAll(tokens);

            // clear tokens for next input
            tokens.clear();

            stringCounter++;
        }

    }

    // ---------------------------------------
    // SKIP WHITESPACE
    //
    // skips spaces, tabs and newlines
    // ---------------------------------------
    protected void skipWhiteSpace() {
        while (userInput.charAt(currentIndex) == '\t' || userInput.charAt(currentIndex) == '\n'
                || userInput.charAt(currentIndex) == '\n') {
            currentIndex++;

            if (currentIndex >= userInput.length()) {
                break;
            }
        }
    }

    /**
     * ------------------------------------------------
     * PARSE IDENTIFIER
     * 
     * parses variable identifiers and
     * non keywords
     * ------------------------------------------------
     */
    protected void parseIdentifier() {
        String str = "";
        while (true) {
            str += userInput.charAt(currentIndex);

            currentIndex++;

            if (currentIndex >= userInput.length()) {
                break;
            }

            // check if operator is next char
            if (operators.containsKey(Character.toString(userInput.charAt(currentIndex)))) {
                break;
            }

            // check if brace etc is next char
            if (bracesBracketsEtc.containsKey(Character.toString(userInput.charAt(currentIndex)))) {
                break;
            }

            // check for space, tab, or newline, commas
            if (userInput.charAt(currentIndex) == '\t' || userInput.charAt(currentIndex) == '\n'
                    || userInput.charAt(currentIndex) == ' ' || userInput.charAt(currentIndex) == ',') {
                break;
            }
        }

        if (keywords.containsKey(str)) {
            tokens.add(new Token("keyword", str));
        } else {
            tokens.add(new Token("identifier", str));
        }
    }

    /**
     * ------------------------------------------------
     * READ INPUT
     * 
     * reads a single line of input
     * from the command line
     * ------------------------------------------------
     */
    public void readInput(String prompt) {
        System.out.println(prompt);
        userInput = scanner.nextLine();
        currentIndex = 0;
    }

    /**
     * ------------------------------------------------
     * GET USER INPUT
     * 
     * returns the current line of user input
     * ------------------------------------------------
     */
    public String getUserInput() {
        return this.userInput;
    }

    /**
     * ------------------------------------------------
     * PRINT TOKENS
     * 
     * prints the current tokens parsed
     * ------------------------------------------------
     */
    public void printTokens() {
        for (int i = 0; i < tokens.size(); i++) {
            System.out.println(
                    "TOKEN#" + Integer.toString(i + 1) + " " + tokens.get(i).value + ": " + tokens.get(i).type);
        }
    }
}
