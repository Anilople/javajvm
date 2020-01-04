package com.github.anilople.javajvm.instructions.extended;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.NullReference;
import com.github.anilople.javajvm.utils.ByteUtils;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;

/**
 * Operation
 * Branch if reference not null
 *
 * Operand ..., value →
 * Stack ...
 *
 * Description
 * The value must be of type reference . It is popped from the
 * operand stack. If value is not null , the unsigned branchbyte1 and
 * branchbyte2 are used to construct a signed 16-bit offset, where
 * the offset is calculated to be (branchbyte1 << 8) | branchbyte2.
 * Execution then proceeds at that offset from the address of the
 * opcode of this ifnonnull instruction. The target address must be
 * that of an opcode of an instruction within the method that contains
 * this ifnonnull instruction.
 * Otherwise, execution proceeds at the address of the instruction
 * following this ifnonnull instruction.
 */
public class IFNONNULL implements Instruction {

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
        if(! (value instanceof NullReference)) {
            // if value is not null
            // a signed 16-bit offset
            short offset = ByteUtils.bytes2short(this.branchByte1, this.branchByte2);
            int nowPc = frame.getNextPc();
            int nextPc = nowPc + offset;
            frame.setNextPc(nextPc);
            return;
        }

        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 3;
    }

}
