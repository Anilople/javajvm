package com.github.anilople.javajvm.instructions.loads;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;

/**
 * Operation
 * Load long from local variable
 *
 * Operand ... →
 * Stack ..., value
 *
 * Description
 * The index is an unsigned byte. Both index and index+1 must be
 * indices into the local variable array of the current frame (§2.6).
 * The local variable at index must contain a long . The value of the
 * local variable at index is pushed onto the operand stack.
 *
 * Notes
 * The lload opcode can be used in conjunction with the wide
 * instruction (§wide) to access a local variable using a two-byte
 * unsigned index.
 */
public class LLOAD implements Instruction {

    private byte unsignedByteIndex;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.unsignedByteIndex = bytecodeReader.readU1();
    }

    @Override
    public void execute(Frame frame) {
        int index = PrimitiveTypeUtils.intFormUnsignedByte(unsignedByteIndex);
        LLOAD.execute(this, frame, index);
    }

    @Override
    public int size() {
        return 2;
    }

    /**
     * load the long value in position index of local variables to
     * the top of operand stack
     * @param instruction
     * @param frame
     * @param index
     * @return
     */
    public static void execute(Instruction instruction, Frame frame, int index) {
        long longValue = frame.getLocalVariables().getLongValue(index);
        frame.getOperandStacks().pushLongValue(longValue);
        int nextPc = frame.getNextPc() + instruction.size();
        frame.setNextPc(nextPc);
    }

}
