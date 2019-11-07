package com.github.anilople.javajvm.instructions.constants;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

/**
 * Operation:
 * Push item from run-time constant pool (wide index)
 */
public class LDC implements Instruction {

    private short index;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.index = bytecodeReader.readU2();
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
