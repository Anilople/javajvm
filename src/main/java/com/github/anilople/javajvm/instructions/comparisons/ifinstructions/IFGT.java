package com.github.anilople.javajvm.instructions.comparisons.ifinstructions;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.utils.ByteUtils;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;

/**
 * Operation
 * Branch if int comparison with zero succeeds
 *
 * Operand ..., value →
 * Stack ...
 *
 * Description
 * The value must be of type int . It is popped from the operand
 * stack and compared against zero.
 * All comparisons are signed. The
 * results of the comparisons are as follows:
 * • ifeq succeeds if and only if value = 0
 * • ifne succeeds if and only if value ≠ 0
 * • iflt succeeds if and only if value < 0
 * • ifle succeeds if and only if value ≤ 0
 * • ifgt succeeds if and only if value > 0
 * • ifge succeeds if and only if value ≥ 0
 *
 * If the comparison succeeds, the unsigned branchbyte1 and
 * branchbyte2 are used to construct a signed 16-bit offset, where
 * the offset is calculated to be (branchbyte1 << 8) | branchbyte2.
 *
 * Execution then proceeds at that offset from the address of the
 * opcode of this if<cond> instruction.
 *
 * The target address must be that of an opcode of an instruction within the method that contains
 * this if<cond> instruction.
 * Otherwise, execution proceeds at the address of the instruction
 * following this if<cond> instruction.
 */
public class IFGT implements Instruction {

    private byte branchByte1;

    private byte branchByte2;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.branchByte1 = bytecodeReader.readU1();
        this.branchByte2 = bytecodeReader.readU1();
    }

    @Override
    public int execute(Frame frame) {
        int value = frame.getOperandStacks().popIntValue();
        if(value > 0) {
            // comparison succeeds
            int branchOffset = PrimitiveTypeUtils.intFormSignedShort(ByteUtils.bytes2short(branchByte1, branchByte2));
            int nextPc = frame.getNextPc() + branchOffset;
            frame.setNextPc(nextPc);
        } else {
            int nextPc = frame.getNextPc() + this.size();
            frame.setNextPc(nextPc);
        }
        return frame.getJvmThread().getPc() + this.size();
    }

    @Override
    public int size() {
        return 3;
    }

}
