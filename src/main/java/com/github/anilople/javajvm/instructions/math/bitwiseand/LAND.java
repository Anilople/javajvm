package com.github.anilople.javajvm.instructions.math.bitwiseand;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

/**
 * Operation
 * Boolean AND long
 *
 * Operand ..., value1, value2 →
 * Stack ..., result
 *
 * Description
 * Both value1 and value2 must be of type long . They are popped
 * from the operand stack. A long result is calculated by taking the
 * bitwise AND of value1 and value2. The result is pushed onto the
 * operand stack.
 */
public class LAND implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void execute(Frame frame) {
        final long value2 = frame.getOperandStacks().popLongValue();
        final long value1 = frame.getOperandStacks().popLongValue();

        final long result = value1 & value2;
        frame.getOperandStacks().pushLongValue(result);

        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 1;
    }

}
