package com.github.anilople.javajvm.instructions.stores;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;

/**
 * Operation
 * Store long into local variable
 *
 * Operand ..., value →
 * Stack ...
 *
 * Description
 * The index is an unsigned byte. Both index and index+1 must be
 * indices into the local variable array of the current frame (§2.6).
 * The value on the top of the operand stack must be of type long . It
 * is popped from the operand stack, and the local variables at index
 * and index+1 are set to value.
 *
 * Notes
 * The lstore opcode can be used in conjunction with the wide
 * instruction (§wide) to access a local variable using a two-byte
 * unsigned index.
 */
public class LSTORE implements Instruction {

    private byte unsignedByteIndex;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.unsignedByteIndex = bytecodeReader.readU1();
    }

    @Override
    public void execute(Frame frame) {
        int index = PrimitiveTypeUtils.intFormUnsignedByte(this.unsignedByteIndex);
        LSTORE.execute(this, frame, index);
    }

    @Override
    public int size() {
        return 2;
    }

    /**
     * Store the long value from the top of operand stack in
     * the position index of local variables
     *
     * @param instruction
     * @param frame
     * @param index
     * @return
     */
    public static void execute(Instruction instruction, Frame frame, int index) {
        long longValue = frame.getOperandStacks().popLongValue();
        frame.getLocalVariables().setLongValue(index, longValue);
        int nextPc = frame.getNextPc() + instruction.size();
        frame.setNextPc(nextPc);
    }

}
