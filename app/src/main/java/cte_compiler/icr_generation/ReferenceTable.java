package cte_compiler.icr_generation;

import java.util.Hashtable;

import cte_compiler.grammar_enums.OPERATORS;

public class ReferenceTable {
    private Hashtable<String, ThreeAddressCode> refTable;
    private int refCount;

    public ReferenceTable() {
        this.refTable = new Hashtable<String, ThreeAddressCode>();
        refCount = 0;
    }

    public boolean addRef(String refName, OPERATORS op, TACArg arg1, TACArg arg2) {
        if (!this.refExists(refName)) {
            refTable.put(refName, new ThreeAddressCode(refName, op, arg1, arg2));
            refCount++;
            return true;
        }

        return false;
    }

    public boolean removeRef(String refName) {
        if (this.refExists(refName)) {
            refTable.remove(refName);
        }

        return false;
    }

    public boolean refExists(String refName) {
        return this.refTable.containsKey(refName);
    }

    public ThreeAddressCode getRef(String refName) {
        if (this.refExists(refName)) {
            return refTable.get(refName);
        }

        return null;
    }

    public int getRefCount() {
        return this.refCount;
    }

}
