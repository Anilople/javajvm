package com.github.anilople.javajvm.instructions.math.remainder;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

public class IREM implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public int execute(Frame frame) {
        return frame.getJvmThread().getPc() + this.size();

    }

    @Override
    public int size() {
        return 1;
    }

}