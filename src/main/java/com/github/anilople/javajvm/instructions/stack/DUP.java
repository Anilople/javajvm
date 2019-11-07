package com.github.anilople.javajvm.instructions.stack;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.LocalVariable;
import com.github.anilople.javajvm.runtimedataarea.OperandStacks;

/**
 * Operation:
 * Duplicate the top operand stack value
 * Description:
 * Duplicate the top value on the operand stack and push the
 * duplicated value onto the operand stack.
 * The dup instruction must not be used unless value is a value of a
 * category 1 computational type (§2.11.1).
 */
public class DUP implements Instruction {
    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public int execute(Frame frame) {
        OperandStacks operandStacks = frame.getOperandStacks();

        // pop once
        LocalVariable localVariable = operandStacks.pop();

        // push twice
        operandStacks.push(localVariable);
        operandStacks.push(localVariable);

        return this.size();
    }

    @Override
    public int size() {
        return 1;
    }
}
