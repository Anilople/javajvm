package com.github.anilople.javajvm.instructions.math.remainder;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

/**
 * Operation
 * Remainder double
 *
 * Operand ..., value1, value2 →
 * Stack ..., result
 *
 * Description
 * Both value1 and value2 must be of type double . The values are
 * popped from the operand stack and undergo value set conversion
 * (§2.8.3), resulting in value1' and value2'. The result is calculated
 * and pushed onto the operand stack as a double
 *
 *
 */
public class DREM implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void execute(Frame frame) {
        double value2 = frame.getOperandStacks().popDoubleValue();
        double value1 = frame.getOperandStacks().popDoubleValue();
        double result = value1 % value2;
        frame.getOperandStacks().pushDoubleValue(result);
        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 1;
    }

}
