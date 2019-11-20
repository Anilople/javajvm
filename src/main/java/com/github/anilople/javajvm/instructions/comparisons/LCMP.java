package com.github.anilople.javajvm.instructions.comparisons;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.OperandStacks;

/**
 * Operation:
 * Compare long
 * Operand: ..., value1, value2 →
 * Stack: ..., result
 * Description:
 * Both value1 and value2 must be of type long . They are both
 * popped from the operand stack, and a signed integer comparison
 * is performed. If value1 is greater than value2, the int value 1 is
 * pushed onto the operand stack. If value1 is equal to value2, the
 * int value 0 is pushed onto the operand stack. If value1 is less than
 * value2, the int value -1 is pushed onto the operand stack.
 */
public class LCMP implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public int execute(Frame frame) {
        OperandStacks operandStacks = frame.getOperandStacks();
        long value2 = operandStacks.popLongValue();
        long value1 = operandStacks.popLongValue();

        if (value1 > value2) {
            operandStacks.pushIntValue(1);
        } else if (value1 == value2) {
            operandStacks.pushIntValue(0);
        } else {
            operandStacks.pushIntValue(-1);
        }
        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
        return frame.getJvmThread().getPc() + this.size();
    }

    @Override
    public int size() {
        return 1;
    }
}
