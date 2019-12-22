package com.github.anilople.javajvm.instructions.constants;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

/**
 * Operation: Push byte
 *
 * Operand ... →
 * Stack ..., value
 *
 * Description:
 * The immediate byte is sign-extended to an int value. That value
 * is pushed onto the operand stack.
 */
public class BIPUSH implements Instruction {

    private byte byteValue;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.byteValue = bytecodeReader.readU1();
    }

    @Override
    public void execute(Frame frame) {
        // sign-extended
        int intValue = (int) byteValue;
        frame.getOperandStacks().pushIntValue(intValue);
        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 2;
    }
}
