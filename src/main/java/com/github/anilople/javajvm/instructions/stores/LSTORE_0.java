package com.github.anilople.javajvm.instructions.stores;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

public class LSTORE_0 implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void execute(Frame frame) {
        LSTORE.execute(this, frame, 0);
    }

    @Override
    public int size() {
        return 1;
    }

}
