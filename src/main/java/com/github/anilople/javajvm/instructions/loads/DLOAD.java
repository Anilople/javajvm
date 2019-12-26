package com.github.anilople.javajvm.instructions.loads;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;

/**
 * Operation
 * Load double from local variable
 *
 * Operand ... →
 * Stack ..., value
 *
 * Description
 * The index is an unsigned byte. Both index and index+1 must be
 * indices into the local variable array of the current frame (§2.6).
 * The local variable at index must contain a double . The value of
 * the local variable at index is pushed onto the operand stack.
 *
 * Notes
 * The dload opcode can be used in conjunction with the wide
 * instruction (§wide) to access a local variable using a two-byte
 * unsigned index.
 */
public class DLOAD implements Instruction {

    private byte index;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.index = bytecodeReader.readU1();
    }

    @Override
    public void execute(Frame frame) {
        int intIndex = PrimitiveTypeUtils.intFormUnsignedByte(index);
        DLOAD.execute(this, frame, intIndex);
    }

    @Override
    public int size() {
        return 2;
    }

    /**
     * load the double value from local variable to
     * the top of operand stack
     * @param instruction
     * @param frame
     * @param index
     */
    public static void execute(Instruction instruction, Frame frame, int index) {
        // get value from local variables
        double value = frame.getLocalVariables().getDoubleValue(index);
        // push the value to operand stack
        frame.getOperandStacks().pushDoubleValue(value);
        // update the pc register
        int nextPc = frame.getNextPc() + instruction.size();
        frame.setNextPc(nextPc);
    }

}
