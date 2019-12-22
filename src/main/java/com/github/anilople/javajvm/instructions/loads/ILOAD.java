package com.github.anilople.javajvm.instructions.loads;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.LocalVariables;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;

/**
 * Operation:
 * Load int from local variable
 * Description:
 * The index is an unsigned byte that must be an index into the local
 * variable array of the current frame (§2.6). The local variable at
 * index must contain an int . The value of the local variable at index
 * is pushed onto the operand stack.
 * Notes
 * The iload opcode can be used in conjunction with the wide
 * instruction (§wide) to access a local variable using a two-byte
 * unsigned index.
 */
public class ILOAD implements Instruction {

    private byte unsignedByteIndex;

    /**
     * get an int value from local variables (by index)
     * then push this int value to the top of operand stack
     *
     * @param frame
     * @param index
     * @return how many bytes this instruction occupies
     */
    public static void execute(Instruction instruction, Frame frame, int index) {
        LocalVariables localVariables = frame.getLocalVariables();
        int value = localVariables.getIntValue(index);
        frame.getOperandStacks().pushIntValue(value);
        int nextPc = frame.getNextPc() + instruction.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.unsignedByteIndex = bytecodeReader.readU1();
    }

    @Override
    public void execute(Frame frame) {
        int index = PrimitiveTypeUtils.intFormUnsignedByte(this.unsignedByteIndex);
        execute(this, frame, index);
    }

    @Override
    public int size() {
        return 2;
    }
}
