package com.github.anilople.javajvm.instructions.math.multiply;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

public class LMUL implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void execute(Frame frame) {
        long value2 = frame.getOperandStacks().popLongValue();
        long value1 = frame.getOperandStacks().popLongValue();

        long result = value1 * value2;
        frame.getOperandStacks().pushLongValue(result);

        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 1;
    }

}
