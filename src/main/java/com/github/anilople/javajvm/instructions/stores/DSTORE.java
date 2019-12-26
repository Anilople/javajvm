package com.github.anilople.javajvm.instructions.stores;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;

/**
 * Operation
 * Store double into local variable
 *
 * Operand ..., value →
 * Stack ...
 *
 * Description
 * The index is an unsigned byte. Both index and index+1 must be
 * indices into the local variable array of the current frame (§2.6).
 * The value on the top of the operand stack must be of type double .
 * It is popped from the operand stack and undergoes value set
 * conversion (§2.8.3), resulting in value'. The local variables at index
 * and index+1 are set to value'.
 *
 * Notes
 * The dstore opcode can be used in conjunction with the wide
 * instruction (§wide) to access a local variable using a two-byte
 * unsigned index.
 */
public class DSTORE implements Instruction {

    private byte index;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.index = bytecodeReader.readU1();
    }

    @Override
    public void execute(Frame frame) {
        int intIndex = PrimitiveTypeUtils.intFormUnsignedByte(index);
        ISTORE.execute(this, frame, intIndex);
    }

    @Override
    public int size() {
        return 2;
    }

    /**
     * for DSTORE_i using
     * store the double value to local variables
     * @param instruction
     * @param frame
     * @param index
     */
    public static void execute(Instruction instruction, Frame frame, int index) {
        // get the double value
        double value = frame.getOperandStacks().popDoubleValue();
        // store it to local variables
        frame.getLocalVariables().setDoubleValue(index, value);

        // update pc register
        int nextPc = frame.getNextPc() + instruction.size();
        frame.setNextPc(nextPc);
    }
}
