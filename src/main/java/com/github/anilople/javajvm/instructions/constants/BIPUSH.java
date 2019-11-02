package com.github.anilople.javajvm.instructions.constants;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

/**
 * Operation: Push byte
 * Description:
 *      The immediate byte is sign-extended to an int value. That value
 *      is pushed onto the operand stack.
 */
public class BIPUSH implements Instruction {

    private byte byteValue;

    @Override
    public void FetchOperands(BytecodeReader bytecodeReader) {
        this.byteValue = bytecodeReader.readU1();
    }

    @Override
    public void Execute(Frame frame) {
        int intValue = (int) byteValue;
        frame.getOperandStacks().pushIntValue(intValue);
    }
}
