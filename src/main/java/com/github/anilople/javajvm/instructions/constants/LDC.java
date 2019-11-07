package com.github.anilople.javajvm.instructions.constants;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

/**
 * Operation:
 * Push item from run-time constant pool (wide index)
 * Description:
 * The unsigned indexbyte1 and indexbyte2 are assembled into an
 * unsigned 16-bit index into the run-time constant pool of the
 * current class (§2.6),
 */
public class LDC implements Instruction {

//    private byte indexByte1;

//    private byte indexByte2;

    // use a short replace 2 bytes above
    private short index;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.index = bytecodeReader.readU2();
    }

    @Override
    public int execute(Frame frame) {
        return this.size();

    }

    @Override
    public int size() {
        return 1;
    }
}
