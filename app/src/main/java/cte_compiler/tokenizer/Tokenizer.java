package cte_compiler.tokenizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.checkerframework.checker.units.qual.s;

/**
 * Tokenizer class takes in user input and converts it into individual tokens
 * which can then be fed to the semantic analyzer
 * 
 * @author John Adriaans
 * @version 0.1.0
 * @since 2022-04-10
 */

public class Tokenizer {

    private Scanner scanner; // reads command line input
    private String userInput; // stores input read
    private int currentIndex; // keeps track of current position in input string
    private int currentLineNumber = 1; // tracks current line number

    private ArrayList<Token> tokens;

    private HashMap<String, String> keywords;
    private HashMap<String, String> operators;
    private HashMap<String, String> symbols;
    private HashMap<String, String> comparators;

    // ---- CONSTRUCTOR ----

    public Tokenizer(HashMap<String, String> keywords, HashMap<String, String> operators,
            HashMap<String, String> symbols, HashMap<String, String> comparators) {

        // init input variables
        this.scanner = new Scanner(System.in);
        this.userInput = "";
        this.currentIndex = 0;

        // init token related variables
        this.tokens = new ArrayList<>();

        // set maps to values passed in
        this.keywords = keywords;
        this.operators = operators;
        this.symbols = symbols;
        this.comparators = comparators;
    }

    /**
     * -------------------------------------------------
     * CREATE TOKENS
     * 
     * creates tokens from the input provided by the
     * 
     * @return void
     * @
     * -------------------------------------------------
     */
    public void createTokens() {

        // loop until end of input
        while (true) {

            // skip whitespace
            skipWhiteSpace();

            // check for comparator
            parseComparator();

            // check for number
            parseNumber();

            // check for string literal
            parseStringLiteral();

            /**
             * code above changes index while code below
             * makes use of index which might be out of bounds
             */
            if (currentIndex >= userInput.length()) {
                break;
            }

            // check for symbol
            if (this.symbols.containsKey(Character.toString(userInput.charAt(currentIndex)))) {
                tokens.add(new Token(TOKEN_TYPES.SYMBOL,
                        Character.toString(userInput.charAt(currentIndex)), currentLineNumber));
            }

            // check for operator
            if (operators.containsKey(Character.toString(userInput.charAt(currentIndex)))) {
                tokens.add(new Token(TOKEN_TYPES.OPERATOR,
                        Character.toString(userInput.charAt(currentIndex)), currentLineNumber));
            }

            // check for word
            if (Character.isAlphabetic(userInput.charAt(currentIndex))) {
                parseIdentifier();
                continue; // move to next iteration of loop
            }

            // move to next char
            currentIndex++;
        }
    }

    // ---------------------------------------
    // SKIP WHITESPACE
    //
    // skips spaces, tabs and newlines
    // ---------------------------------------
    private void skipWhiteSpace() {
        if (currentIndex >= userInput.length()) {
            return;
        }

        while (userInput.charAt(currentIndex) == '\t' || userInput.charAt(currentIndex) == '\n'
                || userInput.charAt(currentIndex) == '\n') {

            checkForNewLine();

            currentIndex++;

            if (currentIndex >= userInput.length()) {
                break;
            }
        }
    }

    /**
     * ------------------------------------------------
     * CHECK FOR NEW LINE
     * 
     * checks for newline char and increases
     * current line number if found
     * ------------------------------------------------
     */
    private void checkForNewLine() {
        // increase line number if newline char found
        if (userInput.charAt(currentIndex) == '\n') {
            currentLineNumber++;
        }
    }

    /**
     * ------------------------------------------------
     * PARSE IDENTIFIER
     * 
     * parses variable identifiers and keywords
     * ------------------------------------------------
     */
    private void parseIdentifier() {
        String str = "";
        while (true) {
            str += userInput.charAt(currentIndex);

            currentIndex++;

            if (currentIndex >= userInput.length()) {
                break;
            }

            // check if next char is operator
            if (operators.containsKey(Character.toString(userInput.charAt(currentIndex)))) {
                break;
            }

            // check if next char is symbol
            if (symbols.containsKey(Character.toString(userInput.charAt(currentIndex)))) {
                break;
            }

            // check if next char is comparator
            if (comparators.containsKey(Character.toString(userInput.charAt(currentIndex)))) {
                break;
            }

            // check if brace etc is next char
            // if
            // (bracesBracketsEtc.containsKey(Character.toString(userInput.charAt(currentIndex))))
            // {
            // break;
            // }

            // check for space, tab, or newline, commas
            if (userInput.charAt(currentIndex) == '\t' || userInput.charAt(currentIndex) == '\n'
                    || userInput.charAt(currentIndex) == ' ' || userInput.charAt(currentIndex) == ',') {
                break;
            }
        }

        if (this.keywords.containsKey(str)) {
            tokens.add(new Token(TOKEN_TYPES.KEYWORD, str, currentLineNumber));
        } else {
            tokens.add(new Token(TOKEN_TYPES.IDENTIFIER, str, currentLineNumber));
        }
    }

    /**
     * ------------------------------------------------
     * PARSE STRING LITERAL
     * 
     * parses string literals
     * ------------------------------------------------
     */
    private void parseStringLiteral() {
        if (currentIndex >= userInput.length()) {
            return;
        }

        // check if currnet char is quote
        if (userInput.charAt(currentIndex) == '"') {

            String str = "";

            // loop until end of literal found
            while (true) {

                currentIndex++;
                if (currentIndex >= userInput.length()) {
                    System.out.println("End of string not found! Expected closing \" ");
                    return;
                }

                if (userInput.charAt(currentIndex) == '"') {
                    break;
                }

                str += userInput.charAt(currentIndex);
            }

            tokens.add(new Token(TOKEN_TYPES.LITERAL, str, currentLineNumber));
        }
    }

    /**
     * ------------------------------------------------
     * PARSE NUMBER
     * 
     * parses numbers
     * ------------------------------------------------
     */
    private void parseNumber() {
        if (currentIndex >= userInput.length()) {
            return;
        }

        String number = "";
        while (true) {
            // check if char is digit
            if (!Character.isDigit(userInput.charAt(currentIndex))) {
                break;
            }

            number += userInput.charAt(currentIndex);

            currentIndex++;

            if (currentIndex >= userInput.length()) {
                break;
            }
        }

        if (number.length() > 0) {
            tokens.add(new Token(TOKEN_TYPES.NUMBER, number, currentLineNumber));
        }
    }

    /**
     * ------------------------------------------------
     * PARSE COMPARATOR
     * 
     * parses comparator values
     * ------------------------------------------------
     */
    private void parseComparator() {
        if (currentIndex >= userInput.length()) {
            return;
        }

        String comp = "";

        // check if there is more input
        if (currentIndex + 1 < userInput.length()) {

            // check single char
            comp = Character.toString(userInput.charAt(currentIndex))
                    + Character.toString(userInput.charAt(currentIndex + 1));

            // check if comp is comparator
            if (comparators.containsKey(comp)) {
                tokens.add(new Token(TOKEN_TYPES.COMPARATOR, comp, currentLineNumber));
                currentIndex += 2;
                return;
            }
        }

        // check single char
        comp = Character.toString(userInput.charAt(currentIndex));

        // check if single char is a comparator
        if (comparators.containsKey(comp)) {
            tokens.add(new Token(TOKEN_TYPES.COMPARATOR, comp, currentLineNumber));
            currentIndex++;
        }

    }

    /**
     * ------------------------------------------------
     * READ INPUT
     * 
     * reads source program line by line from the command line.
     * Stores entire input input in userInput class variable
     * 
     * @param prompt
     *                   message to display to user to indicate
     *                   what they should do
     * @param donePrompt
     *                   value the user should enter to finish input
     * @return void
     * @
     * ------------------------------------------------
     */
    public void readInput(String prompt, String donePrompt) {
        System.out.println("\n" + prompt);
        System.out.println("\nEnter " + donePrompt + " to finish input.");

        int lineNumber = 1; // display line number for user
        String line = ""; // store current lines input

        // loop until user enters done value
        while (true) {
            // print line number
            System.out.print(Integer.toString(lineNumber) + ": ");

            // read current line and add to rest of input
            line = scanner.nextLine();

            // exit loop if done value is entered
            if (donePrompt.toLowerCase().equals(line.strip().toLowerCase())) {
                break;
            }

            // add line to rest of input
            userInput += line + "\n";

            lineNumber++;
        }

        // set current index to 0 incase it was changed
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
     * GET TOKENS
     * 
     * returns tokens stored in tokens variable
     * ------------------------------------------------
     */
    public ArrayList<Token> getTokens() {
        return this.tokens;
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
