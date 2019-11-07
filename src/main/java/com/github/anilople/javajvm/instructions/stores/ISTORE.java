package com.github.anilople.javajvm.instructions.stores;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;

/**
 * Operation:
 * Store int into local variable
 * Description:
 * The index is an unsigned byte that must be an index into the local
 * variable array of the current frame (§2.6). The value on the top
 * of the operand stack must be of type int . It is popped from the
 * operand stack, and the value of the local variable at index is set
 * to value.
 * Notes:
 * The istore opcode can be used in conjunction with the wide
 * instruction (§wide) to access a local variable using a two-byte
 * unsigned index.
 */
public class ISTORE implements Instruction {

    private int index;

    public static void Execute(Frame frame, int index) {
        int value = frame.getLocalVariables().getIntValue(index);
        frame.getOperandStacks().pushIntValue(value);
    }

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        byte unsignedByte = bytecodeReader.readU1();
        this.index = PrimitiveTypeUtils.intFormUnsignedByte(unsignedByte);
    }

    @Override
    public int execute(Frame frame) {
        ISTORE.Execute(frame, this.index);
        return this.size();
    }

    @Override
    public int size() {
        return 1;
    }

}
