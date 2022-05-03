package cte_compiler.icr_generation;

import java.util.ArrayList;

import cte_compiler.syntax_analyzer.SyntaxNode;
import cte_compiler.tokenizer.TOKEN_TYPES;

public class IcrGenerator {

    private ReferenceTable referenceTable;
    private ArrayList<String> referenceQueue;

    public IcrGenerator() {
        this.referenceTable = new ReferenceTable();
        this.referenceQueue = new ArrayList<String>();
    }

    public void generate(SyntaxNode root) {
        createReference(root);
    }

    private TACArg createReference(SyntaxNode node) {

        // check for op
        if (node.token.type == TOKEN_TYPES.OPERATOR) {

            // get left and right arguments
            TACArg leftArg = createReference(node.left);
            TACArg rightArg = createReference(node.right);

            // create ref name and add new ref to ref table
            String refName = "t" + Integer.toString(this.referenceTable.getRefCount() + 1);
            this.referenceTable.addRef(refName, node.value, leftArg, rightArg);

            // add ref to ref stack
            referenceQueue.add(refName);

            // return reference name as tac arg e.g t1
            return new TACArg(refName, TOKEN_TYPES.OPERATOR);

        } else { // node is a number

            // return number as tac arg e.g 9
            return new TACArg(node.value, TOKEN_TYPES.NUMBER);
        }

    }

    public ArrayList<String> getReferenceQueue() {
        return this.referenceQueue;
    }

    public ReferenceTable getReferenceTable() {
        return this.referenceTable;
    }

    public void printIcr() {
        for (String refName : referenceQueue) {
            ThreeAddressCode tac = this.referenceTable.getRef(refName);
            if (tac != null) {
                System.out.println(tac.toString());
            }
        }
    }

}
