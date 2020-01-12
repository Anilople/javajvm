package com.github.anilople.javajvm.instructions.math.bitwiseand;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

/**
 * Operation
 * Boolean AND int
 *
 * Operand ..., value1, value2 →
 * Stack ..., result
 *
 * Description
 * Both value1 and value2 must be of type int . They are popped
 * from the operand stack. An int result is calculated by taking the
 * bitwise AND (conjunction) of value1 and value2. The result is
 * pushed onto the operand stack.
 */
public class IAND implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void execute(Frame frame) {
        final int value2 = frame.getOperandStacks().popIntValue();
        final int value1 = frame.getOperandStacks().popIntValue();

        final int result = value1 & value2;

        frame.getOperandStacks().pushIntValue(result);

        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 1;
    }

}
