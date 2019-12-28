package com.github.anilople.javajvm.instructions.constants;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.utils.ByteUtils;

import java.util.Arrays;

/**
 * Operation: Push short
 * Description:
 * The immediate unsigned byte1 and byte2 values are assembled into
 * an intermediate short , where the value of the short is (byte1 <<
 * 8) | byte2. The intermediate value is then sign-extended to an int
 * value. That value is pushed onto the operand stack.
 */
public class SIPUSH implements Instruction {

    private byte byte1;

    private byte byte2;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.byte1 = bytecodeReader.readU1();
        this.byte2 = bytecodeReader.readU1();
    }

    @Override
    public void execute(Frame frame) {
        frame.getOperandStacks().pushShortValue(
                ByteUtils.bytes2short(new byte[]{byte1, byte2})
        );
        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 3;
    }
}
