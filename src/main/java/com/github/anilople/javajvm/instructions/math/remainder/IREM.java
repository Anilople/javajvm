package com.github.anilople.javajvm.instructions.math.remainder;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

public class IREM implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void execute(Frame frame) {
        int value2 = frame.getOperandStacks().popIntValue();
        int value1 = frame.getOperandStacks().popIntValue();
        int result = value1 % value2;
        frame.getOperandStacks().pushIntValue(result);
        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 1;
    }

}
