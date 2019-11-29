package com.github.anilople.javajvm.instructions.loads;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.BaseTypeArrayReference;

/**
 * Operation
 * Load char from array
 *
 * Operand ..., arrayref, index →
 * Stack ..., value
 *
 * Description
 * The arrayref must be of type reference and must refer to an array
 * whose components are of type char .
 * The index must be of type
 * int .
 * Both arrayref and index are popped from the operand stack.
 * The component of the array at index is retrieved and zero-extended
 * to an int value.
 * That value is pushed onto the operand stack.
 *
 */
public class CALOAD implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public int execute(Frame frame) {
        int index = frame.getOperandStacks().popIntValue();
        Reference reference = frame.getOperandStacks().popReference();
        Reference.assertIsNotNull(reference);
        BaseTypeArrayReference baseTypeArrayReference = (BaseTypeArrayReference) reference;
        baseTypeArrayReference.assertIndexIsNotOutOfBounds(index);

        char charValue = baseTypeArrayReference.getCharValue(index);
        frame.getOperandStacks().pushCharValue(charValue);

        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
        return frame.getJvmThread().getPc() + this.size();
    }

    @Override
    public int size() {
        return 1;
    }

}
