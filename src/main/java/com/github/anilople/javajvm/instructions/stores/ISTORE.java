package com.github.anilople.javajvm.instructions.stores;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;

/**
 * Operation:
 *      Store int into local variable
 *
 * Operand ..., value →
 * Stack ...
 *
 * Description:
 *      The index is an unsigned byte that must be an index into the local
 *      variable array of the current frame (§2.6). The value on the top
 *      of the operand stack must be of type int . It is popped from the
 *      operand stack, and the value of the local variable at index is set
 *      to value.
 *
 * Notes:
 *      The istore opcode can be used in conjunction with the wide
 *      instruction (§wide) to access a local variable using a two-byte
 *      unsigned index.
 */
public class ISTORE implements Instruction {

    private int index;

    public static int execute(Instruction instruction, Frame frame, int index) {
        int value = frame.getOperandStacks().popIntValue();
        frame.getLocalVariables().setIntValue(index, value);
        int nextPc = frame.getNextPc() + instruction.size();
        frame.setNextPc(nextPc);
        return frame.getJvmThread().getPc() + instruction.size();
    }

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        byte unsignedByte = bytecodeReader.readU1();
        this.index = PrimitiveTypeUtils.intFormUnsignedByte(unsignedByte);
    }

    @Override
    public int execute(Frame frame) {
        return ISTORE.execute(this, frame, this.index);
    }

    @Override
    public int size() {
        return 1;
    }

}
