package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.ArrayReference;

/**
 * Operation
 * Get length of array
 *
 * Operand ..., arrayref →
 * Stack ..., length
 *
 * Description
 * The arrayref must be of type reference and must refer to an array.
 * It is popped from the operand stack. The length of the array it
 * references is determined. That length is pushed onto the operand
 * stack as an int
 */
public class ARRAYLENGTH implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void execute(Frame frame) {
        Reference reference = frame.getOperandStacks().popReference();
        // Run-time Exceptions
        // If the arrayref is null , the arraylength instruction throws a
        // NullPointerException
        if(Reference.NULL.equals(reference)) {
            throw new NullPointerException("arrayref is null");
        }
        ArrayReference arrayReference = (ArrayReference) reference;

        // array length to stack
        int length = arrayReference.length();
        frame.getOperandStacks().pushIntValue(length);

        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 1;
    }

}
