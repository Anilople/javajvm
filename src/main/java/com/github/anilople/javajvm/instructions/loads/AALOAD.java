package com.github.anilople.javajvm.instructions.loads;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.BaseTypeArrayReference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectArrayReference;

/**
 * Operation
 * Load reference from array
 *
 * Operand ..., arrayref, index →
 * Stack ..., value
 *
 * Description
 * The arrayref must be of type reference and must refer to an array
 * whose components are of type reference .
 * The index must be of
 * type int .
 * Both arrayref and index are popped from the operand
 * stack.
 * The reference value in the component of the array at index
 * is retrieved and pushed onto the operand stack.
 */
public class AALOAD implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void execute(Frame frame) {
        int index = frame.getOperandStacks().popIntValue();
        Reference reference = frame.getOperandStacks().popReference();
        Reference.assertIsNotNull(reference);
        ObjectArrayReference objectArrayReference = (ObjectArrayReference) reference;
        objectArrayReference.assertIndexIsNotOutOfBounds(index);

        frame.getOperandStacks().pushReference(
                objectArrayReference.getReference(index)
        );

        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 1;
    }

}
