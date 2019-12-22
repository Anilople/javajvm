package com.github.anilople.javajvm.instructions.loads;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.ArrayReference;
import com.github.anilople.javajvm.runtimedataarea.reference.BaseTypeArrayReference;

/**
 * Operation
 * Load int from array
 *
 * Operand ..., arrayref, index →
 * Stack ..., value
 *
 * Description
 * The arrayref must be of type reference and must refer to an array
 * whose components are of type int . The index must be of type int
 *
 * Both arrayref and index are popped from the operand stack. The
 * int value in the component of the array at index is retrieved and
 * pushed onto the operand stack.
 *
 * Run-time
 * Exceptions
 * If arrayref is null , iaload throws a NullPointerException .
 * Otherwise, if index is not within the bounds of the array
 * referenced by arrayref, the iaload instruction throws an
 * ArrayIndexOutOfBoundsException .
 */
public class IALOAD implements Instruction {

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
        int value = baseTypeArrayReference.getIntValue(index);
        frame.getOperandStacks().pushIntValue(value);

        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 1;
    }

}
