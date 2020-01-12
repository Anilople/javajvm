package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

public class MONITOREXIT implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void execute(Frame frame) {
        throw new RuntimeException("Now cannot support " + this.getClass());
//        int nextPc = frame.getNextPc() + this.size();
//        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 1;
    }

}
