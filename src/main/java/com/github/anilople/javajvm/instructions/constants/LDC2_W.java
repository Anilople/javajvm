package com.github.anilople.javajvm.instructions.constants;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

public class LDC2_W implements Instruction {

//    private byte indexbyte1;

//    private byte indexbyte2;

    // use short replace above 2 bytes
    private short index;

    @Override
    public void FetchOperands(BytecodeReader bytecodeReader) {
        this.index = bytecodeReader.readU2();
    }

    @Override
    public void Execute(Frame frame) {

    }
}
