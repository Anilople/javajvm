package com.github.anilople.javajvm.instructions.comparisons.ifinstructions;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

public abstract class IF implements Instruction {

//    private byte branchbyte1;

//    private byte branchbyte2;

    // use a short to replace 2 bytes above
    private short branchShort;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.branchShort = bytecodeReader.readU2();
    }

    @Override
    public int execute(Frame frame) {
        return this.size();

    }

    @Override
    public int size() {
        return 3;
    }

}
