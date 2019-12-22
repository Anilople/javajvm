package com.github.anilople.javajvm.instructions.stores;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.BaseTypeArrayReference;

/**
 * Operation
 * Store into int array
 *
 * Operand ..., arrayref, index, value →
 * Stack ...
 *
 * Description
 * The arrayref must be of type reference and must refer to an array
 * whose components are of type int . Both index and value must be
 * of type int .
 * The arrayref, index, and value are popped from the
 * operand stack. The int value is stored as the component of the
 * array indexed by index.
 *
 * Run-time
 * Exceptions
 * If arrayref is null , iastore throws a NullPointerException .
 * Otherwise, if index is not within the bounds of the array
 * referenced by arrayref, the iastore instruction throws an
 * ArrayIndexOutOfBoundsException .
 */
public class IASTORE implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void execute(Frame frame) {
        int value = frame.getOperandStacks().popIntValue();
        int index = frame.getOperandStacks().popIntValue();

        Reference reference = frame.getOperandStacks().popReference();
        Reference.assertIsNotNull(reference);

        BaseTypeArrayReference baseTypeArrayReference = (BaseTypeArrayReference) reference;
        baseTypeArrayReference.assertIndexIsNotOutOfBounds(index);
        baseTypeArrayReference.setIntValue(index, value);

        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 1;
    }

}
