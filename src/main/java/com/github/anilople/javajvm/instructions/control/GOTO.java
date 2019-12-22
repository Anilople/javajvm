package com.github.anilople.javajvm.instructions.control;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.utils.ByteUtils;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;

/**
 * Operation
 * Branch always
 *
 * Description
 * The unsigned bytes branchbyte1 and branchbyte2 are used to
 * construct a signed 16-bit branchoffset, where branchoffset is
 * (branchbyte1 << 8) | branchbyte2.
 *
 * Execution proceeds at that
 * offset from the address of the opcode of this goto instruction. The
 * target address must be that of an opcode of an instruction within
 * the method that contains this goto instruction.
 */
public class GOTO implements Instruction {

    private byte branchbyte1;

    private byte branchbyte2;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.branchbyte1 = bytecodeReader.readU1();
        this.branchbyte2 = bytecodeReader.readU1();
    }

    @Override
    public void execute(Frame frame) {
        int branchOffset = PrimitiveTypeUtils.intFormSignedShort(ByteUtils.bytes2short(branchbyte1, branchbyte2));

        int nextPc = frame.getNextPc() + branchOffset;
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 3;
    }

}
