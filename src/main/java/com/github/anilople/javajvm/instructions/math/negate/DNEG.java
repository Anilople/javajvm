package com.github.anilople.javajvm.instructions.math.negate;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

public class DNEG implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public int execute(Frame frame) {
        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
        return frame.getJvmThread().getPc() + this.size();

    }

    @Override
    public int size() {
        return 1;
    }

}
