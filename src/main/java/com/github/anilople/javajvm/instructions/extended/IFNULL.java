package com.github.anilople.javajvm.instructions.extended;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.utils.ByteUtils;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;

/**
 * Operation
 * Branch if reference is null
 *
 * Format ifnull
 *      branchbyte1
 *      branchbyte2
 *
 * Operand ..., value →
 * Stack ...
 *
 * Description
 * The value must of type reference . It is popped from the operand
 * stack. If value is null , the unsigned branchbyte1 and branchbyte2
 * are used to construct a signed 16-bit offset, where the offset is
 * calculated to be (branchbyte1 << 8) | branchbyte2. Execution then
 * proceeds at that offset from the address of the opcode of this ifnull
 * instruction. The target address must be that of an opcode of an
 * instruction within the method that contains this ifnull instruction.
 * Otherwise, execution proceeds at the address of the instruction
 * following this ifnull instruction.
 */
public class IFNULL implements Instruction {

    private byte branchByte1;

    private byte branchByte2;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.branchByte1 = bytecodeReader.readU1();
        this.branchByte2 = bytecodeReader.readU1();
    }

    @Override
    public void execute(Frame frame) {
        Reference value = frame.getOperandStacks().popReference();
        if(Reference.isNull(value)) {
            int offset = this.resolveOffset();
            int nowPc = frame.getNextPc();
            int nextPc = nowPc + offset;
            frame.setNextPc(nextPc);
        } else {
            int nextPc = frame.getNextPc() + this.size();
            frame.setNextPc(nextPc);
        }
    }

    @Override
    public int size() {
        return 3;
    }

    public int resolveOffset() {
        // a signed 16-bit offset
        // (branchbyte1 << 8) | branchbyte2
        short offset = ByteUtils.bytes2short(branchByte1, branchByte2);
        return PrimitiveTypeUtils.intFormSignedShort(offset);
    }
}
