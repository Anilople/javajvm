package com.github.anilople.javajvm.instructions.stores;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

public class LSTORE_1 implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void execute(Frame frame) {
        LSTORE.execute(this, frame, 1);
    }

    @Override
    public int size() {
        return 1;
    }

}
