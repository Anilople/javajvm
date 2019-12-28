package com.github.anilople.javajvm.instructions.loads;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.BaseTypeArrayReference;
import com.github.anilople.javajvm.runtimedataarea.reference.NullReference;

/**
 * Operation
 * Load double from array
 *
 * Operand ..., arrayref, index →
 * Stack ..., value
 *
 * Description
 * The arrayref must be of type reference and must refer to an
 * array whose components are of type double . The index must be
 * of type int . Both arrayref and index are popped from the operand
 * stack. The double value in the component of the array at index is
 * retrieved and pushed onto the operand stack.
 *
 * Run-time
 * Exceptions
 * If arrayref is null , daload throws a NullPointerException .
 * Otherwise, if index is not within the bounds of the array
 * referenced by arrayref, the daload instruction throws an
 * ArrayIndexOutOfBoundsException
 */
public class DALOAD implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void execute(Frame frame) {
        int index = frame.getOperandStacks().popIntValue();
        Reference reference = frame.getOperandStacks().popReference();
        Reference.assertIsNotNull(reference);
        BaseTypeArrayReference baseTypeArrayReference = (BaseTypeArrayReference) reference;
        baseTypeArrayReference.assertIndexIsNotOutOfBounds(index);
        double doubleValue = baseTypeArrayReference.getDoubleValue(index);
        frame.getOperandStacks().pushDoubleValue(doubleValue);

        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 1;
    }

}
