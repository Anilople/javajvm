package com.github.anilople.javajvm.instructions.stores;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.BaseTypeArrayReference;

/**
 * Operation
 * Store into char array
 *
 * Operand ..., arrayref, index, value →
 * Stack ...
 *
 * Description
 * The arrayref must be of type reference and must refer to an array
 * whose components are of type char .
 * The index and the value must
 * both be of type int .
 * The arrayref, index, and value are popped
 * from the operand stack.
 * The int value is truncated to a char and
 * stored as the component of the array indexed by index.
 */
public class CASTORE implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public int execute(Frame frame) {
        char charValue = frame.getOperandStacks().popCharValue();
        int index = frame.getOperandStacks().popIntValue();
        Reference reference = frame.getOperandStacks().popReference();
        Reference.assertIsNotNull(reference);
        BaseTypeArrayReference baseTypeArrayReference = (BaseTypeArrayReference) reference;
        baseTypeArrayReference.assertIndexIsNotOutOfBounds(index);

        baseTypeArrayReference.setCharValue(index, charValue);

        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
        return frame.getJvmThread().getPc() + this.size();
    }

    @Override
    public int size() {
        return 1;
    }

}
