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
    private ArrayList<String> errorMessages;
    private boolean isValid;

    private ArrayList<Token> tokens;

    private HashMap<String, String> operators;
    private HashMap<String, String> symbols;

    // ---- CONSTRUCTOR ----

    /**
     * works by checking indivual character
     */

    public Tokenizer(HashMap<String, String> operators, HashMap<String, String> symbols) {

        // init input variables
        this.scanner = new Scanner(System.in);
        this.userInput = "";
        this.currentIndex = 0;

        // init token related variables
        this.tokens = new ArrayList<>();

        // set maps to values passed in
        this.operators = operators;
        this.symbols = symbols;

        this.errorMessages = new ArrayList<String>();
        this.isValid = true;
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
    public boolean createTokens() {

        // loop until end of input
        while (true) {

            // skip whitespace
            skipWhiteSpace();

            // check for number
            parseNumber();

            /**
             * code above changes index while code below
             * makes use of index which might be out of bounds
             */
            if (currentIndex >= userInput.length()) {
                break;
            }

            // check for invalid characters
            checkForInvalidChar();

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

            // move to next char
            currentIndex++;
        }

        if (this.userInput.length() == 0) {
            this.errorMessages.add("no user input!");
            return false;
        }
        if (this.userInput.charAt(userInput.length() - 1) != ';') {
            this.errorMessages.add("semi-colon expected at end of string!");
            return false;
        }

        // false = failure, true = success
        return this.isValid;
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
     * CHECK FOR INVALID CHAR
     * 
     * checks for invalid characters and
     * returns true if found
     * ------------------------------------------------
     */
    private void checkForInvalidChar() {

        char c = userInput.charAt(currentIndex);

        if (Character.isAlphabetic(c)) {
            this.errorMessages.add(Character.toString(c) + ": no letters (aA-zZ) allowed!");
            this.isValid = false;
        }

        // not number, symbol, operator or ;
        if (!Character.isDigit(c) && !operators.containsKey(Character.toString(c))
                && !operators.containsKey(Character.toString(c)) && c != ';') {
            this.errorMessages.add(Character.toString(c) + ": only numbers and operators(+,-,/,*) allowed!");
            this.isValid = false;
        }

        // two operators next to each other
        if (currentIndex + 1 < userInput.length()) {
            char cc = userInput.charAt(currentIndex + 1);
            if (operators.containsKey(Character.toString(c)) && operators.containsKey(Character.toString(cc))) {
                this.errorMessages.add(Character.toString(c) + ", " + Character.toString(cc)
                        + ": two operators cannot be next to each other");
                this.isValid = false;
            }
        }

        // ; at wrong place
        if (c == ';' && currentIndex != userInput.length() - 1) {
            this.errorMessages.add(Character.toString(c) + ": semi-colon only allowed at the end of an expression");
            this.isValid = false;
        }

        // ; not after a number
        if (currentIndex - 1 >= 0) {
            char cc = userInput.charAt(currentIndex - 1);
            if (!Character.isDigit(cc)) {
                this.errorMessages.add(Character.toString(c) + ": semi-colon must follow a digit");
                this.isValid = false;
            }
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
    public boolean readInput(String prompt) {
        System.out.println("\n" + prompt);

        // read current line and add to rest of input
        this.userInput = scanner.nextLine();

        // return false if user entered exit
        if (userInput.toLowerCase().equals("exit"))
            return false;

        // set current index to 0 incase it was changed
        currentIndex = 0;

        return true;
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
     * GET ERROR MESSAGE
     * 
     * returns error message
     * ------------------------------------------------
     */
    public ArrayList<String> getErrorMessages() {
        return this.errorMessages;
    }

    public void printErrorMessages() {
        for (String s : this.errorMessages) {
            System.out.println(s);
        }
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
