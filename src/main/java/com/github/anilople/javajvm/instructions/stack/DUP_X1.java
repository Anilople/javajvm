package com.github.anilople.javajvm.instructions.stack;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.LocalVariable;
import com.github.anilople.javajvm.runtimedataarea.OperandStacks;

/**
 * Operation:
 * Duplicate the top operand stack value and insert two values down
 * Operand:
 * ..., value2, value1 →
 * Stack:
 * ..., value1, value2, value1
 * Description:
 * Duplicate the top value on the operand stack and insert the
 * duplicated value two values down in the operand stack.
 * The dup_x1 instruction must not be used unless both value1 and
 * value2 are values of a category 1 computational type (§2.11.1).
 */
public class DUP_X1 implements Instruction {
    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public int execute(Frame frame) {
        OperandStacks operandStacks = frame.getOperandStacks();

        LocalVariable value1 = operandStacks.pop();
        LocalVariable value2 = operandStacks.pop();

        operandStacks.push(value1);
        operandStacks.push(value2);
        operandStacks.push(value1);

        return this.size();
    }

    @Override
    public int size() {
        return 1;
    }
}
