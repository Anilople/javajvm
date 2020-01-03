package com.github.anilople.javajvm.instructions.math.divide;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

public class FDIV implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void execute(Frame frame) {
        float value2 = frame.getOperandStacks().popFloatValue();
        float value1 = frame.getOperandStacks().popFloatValue();

        float result = value1 / value2;
        frame.getOperandStacks().pushFloatValue(result);

        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 1;
    }

}
