package com.github.anilople.javajvm.instructions.constants;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

/**
 * Operation: Push short
 * Description:
 *      The immediate unsigned byte1 and byte2 values are assembled into
 *      an intermediate short , where the value of the short is (byte1 <<
 *      8) | byte2. The intermediate value is then sign-extended to an int
 *      value. That value is pushed onto the operand stack.
 */
public class SIPUSH implements Instruction {

    private short shortValue;

    @Override
    public void FetchOperands(BytecodeReader bytecodeReader) {
        this.shortValue = bytecodeReader.readU2();
    }

    @Override
    public void Execute(Frame frame) {
        frame.getOperandStacks().pushShortValue(this.shortValue);
    }
}
