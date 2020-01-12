package com.github.anilople.javajvm.instructions.conversions;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

/**
 * Operation
 * Convert long to float
 */
public class L2F implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void execute(Frame frame) {
        long longValue = frame.getOperandStacks().popLongValue();
        float floatValue = (float) longValue;
        frame.getOperandStacks().pushFloatValue(floatValue);
        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 1;
    }

}
