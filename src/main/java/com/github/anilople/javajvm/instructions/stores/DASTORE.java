package com.github.anilople.javajvm.instructions.stores;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.BaseTypeArrayReference;

/**
 * Operation
 * Store into double array
 *
 * Operand ..., arrayref, index, value →
 * Stack ...
 *
 * Description
 * The arrayref must be of type reference and must refer to an
 * array whose components are of type double . The index must be of
 * type int , and value must be of type double . The arrayref, index,
 * and value are popped from the operand stack. The double value
 * undergoes value set conversion (§2.8.3), resulting in value', which
 * is stored as the component of the array indexed by index.
 *
 *
 */
public class DASTORE implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void execute(Frame frame) {
        double doubleValue = frame.getOperandStacks().popDoubleValue();
        int index = frame.getOperandStacks().popIntValue();
        Reference reference = frame.getOperandStacks().popReference();
        Reference.assertIsNotNull(reference);
        BaseTypeArrayReference baseTypeArrayReference = (BaseTypeArrayReference) reference;
        baseTypeArrayReference.assertIndexIsNotOutOfBounds(index);
        baseTypeArrayReference.setDoubleValue(index, doubleValue);

        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 1;
    }

}
