package com.github.anilople.javajvm.instructions.loads;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.BaseTypeArrayReference;
import com.github.anilople.javajvm.runtimedataarea.reference.NullReference;

/**
 * Operation
 * Load short from array
 *
 * Operand ..., arrayref, index →
 * Stack ..., value
 *
 *
 */
public class SALOAD implements Instruction {

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
        short shortValue = baseTypeArrayReference.getShortValue(index);
        frame.getOperandStacks().pushShortValue(shortValue);

        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 1;
    }

}
