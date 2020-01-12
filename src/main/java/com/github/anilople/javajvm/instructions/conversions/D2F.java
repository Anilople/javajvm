package com.github.anilople.javajvm.instructions.conversions;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

/**
 * Operation
 * Convert double to float
 *
 * Operand ..., value →
 * Stack ..., result
 *
 */
public class D2F implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void execute(Frame frame) {
        double doubleValue = frame.getOperandStacks().popDoubleValue();
        float floatValue = (float) doubleValue;
        frame.getOperandStacks().pushFloatValue(floatValue);
        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 1;
    }

}
