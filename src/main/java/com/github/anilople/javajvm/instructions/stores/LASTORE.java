package com.github.anilople.javajvm.instructions.stores;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.BaseTypeArrayReference;

public class LASTORE implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void execute(Frame frame) {
        long longValue = frame.getOperandStacks().popLongValue();
        int index = frame.getOperandStacks().popIntValue();
        Reference reference = frame.getOperandStacks().popReference();
        Reference.assertIsNotNull(reference);
        BaseTypeArrayReference baseTypeArrayReference = (BaseTypeArrayReference) reference;
        baseTypeArrayReference.assertIndexIsNotOutOfBounds(index);
        baseTypeArrayReference.setLongValue(index, longValue);
        
        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 1;
    }

}
