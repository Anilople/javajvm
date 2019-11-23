package com.github.anilople.javajvm.instructions.comparisons.ifinstructions;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.utils.ByteUtils;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;

/**
 * Operation
 * Branch if int comparison succeeds
 *
 * Operand ..., value1, value2 →
 * Stack ...
 *
 * Description
 * Both value1 and value2 must be of type int . They are both popped
 * from the operand stack and compared. All comparisons are signed.
 * The results of the comparison are as follows:
 * • if_icmpeq succeeds if and only if value1 = value2
 * • if_icmpne succeeds if and only if value1 ≠ value2
 * • if_icmplt succeeds if and only if value1 < value2
 * • if_icmple succeeds if and only if value1 ≤ value2
 * • if_icmpgt succeeds if and only if value1 > value2
 * • if_icmpge succeeds if and only if value1 ≥ value2
 *
 * If the comparison succeeds, the unsigned branchbyte1 and
 * branchbyte2 are used to construct a signed 16-bit offset, where
 * the offset is calculated to be (branchbyte1 << 8) | branchbyte2.
 *
 * Execution then proceeds at that offset from the address of the
 * opcode of this if_icmp<cond> instruction.
 *
 * The target address must be that of an opcode of an instruction within the method that
 * contains this if_icmp<cond> instruction.
 * Otherwise, execution proceeds at the address of the instruction
 * following this if_icmp<cond> instruction.
 */
public class IF_ICMPGT implements Instruction {

    private byte branchbyte1;

    private byte branchbyte2;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.branchbyte1 = bytecodeReader.readU1();
        this.branchbyte2 = bytecodeReader.readU1();
    }

    @Override
    public int execute(Frame frame) {
        int value2 = frame.getOperandStacks().popIntValue();
        int value1 = frame.getOperandStacks().popIntValue();
        //  if_icmpgt succeeds if and only if value1 > value2
        if(value1 > value2) {
            // succeed
            short branchOffset = ByteUtils.bytes2short(branchbyte1, branchbyte2);
            int nextPc = frame.getNextPc() + PrimitiveTypeUtils.intFormSignedShort(branchOffset);
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
