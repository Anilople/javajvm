package com.github.anilople.javajvm.instructions.stores;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectReference;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;

/**
 * Operation
 * Store reference into local variable
 *
 * Operand ..., objectref →
 * Stack ...
 *
 * Description
 * The index is an unsigned byte that must be an index into the local
 * variable array of the current frame (§2.6). The objectref on the top
 * of the operand stack must be of type returnAddress or of type
 * reference . It is popped from the operand stack, and the value of
 * the local variable at index is set to objectref.
 *
 * Notes
 * The astore instruction is used with an objectref of type
 * returnAddress when implementing the finally clause of the
 * Java programming language (§3.13).
 * The aload instruction (§aload) cannot be used to load a value of
 * type returnAddress from a local variable onto the operand stack.
 * This asymmetry with the astore instruction is intentional.
 * The astore opcode can be used in conjunction with the wide
 * instruction (§wide) to access a local variable using a two-byte
 * unsigned index.
 */
public class ASTORE implements Instruction {

    private byte unsignedByteIndex;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.unsignedByteIndex = bytecodeReader.readU1();
    }

    @Override
    public int execute(Frame frame) {
        int index = PrimitiveTypeUtils.intFormUnsignedByte(this.unsignedByteIndex);
        ASTORE.execute(this, frame, index);

        return frame.getJvmThread().getPc() + this.size();
    }

    @Override
    public int size() {
        return 2;
    }

    /**
     * store reference into local variable position index
     * @param instruction
     * @param frame
     * @param index
     */
    public static void execute(Instruction instruction, Frame frame, int index) {
        // simply ignore returnAddress
        Reference reference = frame.getOperandStacks().popReference();
        frame.getLocalVariables().setReference(index, reference);
        int nextPc = frame.getNextPc() + instruction.size();
        frame.setNextPc(nextPc);
    }

}
