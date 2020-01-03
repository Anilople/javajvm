package com.github.anilople.javajvm.instructions.stores;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;

/**
 * Operation
 * Store float into local variable
 *
 * Operand ..., value →
 * Stack ...
 *
 * Description
 * The index is an unsigned byte that must be an index into the local
 * variable array of the current frame (§2.6). The value on the top
 * of the operand stack must be of type float . It is popped from
 * the operand stack and undergoes value set conversion (§2.8.3),
 * resulting in value'. The value of the local variable at index is set
 * to value'.
 *
 * Notes
 * The fstore opcode can be used in conjunction with the wide
 * instruction (§wide) to access a local variable using a two-byte
 * unsigned index.
 */
public class FSTORE implements Instruction {

    private byte byteIndex;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.byteIndex = bytecodeReader.readU1();
    }

    @Override
    public void execute(Frame frame) {
        FSTORE.execute(this, frame, resolveIndex());
    }

    @Override
    public int size() {
        return 2;
    }

    public static void execute(Instruction instruction, Frame frame, int index) {
        float value = frame.getOperandStacks().popFloatValue();
        frame.getLocalVariables().setFloatValue(index, value);
        int nextPc = frame.getNextPc() + instruction.size();
        frame.setNextPc(nextPc);
    }

    public int resolveIndex() {
        return PrimitiveTypeUtils.intFormUnsignedByte(byteIndex);
    }
}
