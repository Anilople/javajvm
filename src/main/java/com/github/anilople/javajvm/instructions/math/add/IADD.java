package com.github.anilople.javajvm.instructions.math.add;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.OperandStacks;

/**
 * Operation:
 * Add int
 * <p>
 * Operand: ..., value1, value2 →
 * Stack: ..., result
 * <p>
 * Description:
 * Both value1 and value2 must be of type int . The values are popped
 * from the operand stack. The int result is value1 + value2. The
 * result is pushed onto the operand stack.
 * The result is the 32 low-order bits of the true mathematical result
 * in a sufficiently wide two's-complement format, represented as a
 * value of type int . If overflow occurs, then the sign of the result
 * may not be the same as the sign of the mathematical sum of the
 * two values.
 * Despite the fact that overflow may occur, execution of an iadd
 * instruction never throws a run-time exception.
 */
public class IADD implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public int execute(Frame frame) {
        OperandStacks operandStacks = frame.getOperandStacks();

        int value2 = operandStacks.popIntValue();
        int value1 = operandStacks.popIntValue();

        int result = value1 + value2;

        operandStacks.pushIntValue(result);
        return this.size();
    }

    @Override
    public int size() {
        return 1;
    }
}
