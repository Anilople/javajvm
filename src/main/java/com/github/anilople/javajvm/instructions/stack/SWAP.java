package com.github.anilople.javajvm.instructions.stack;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.LocalVariable;
import com.github.anilople.javajvm.runtimedataarea.OperandStacks;

/**
 * Operation:
 * Swap the top two operand stack values
 * Operand:
 * ..., value2, value1 →
 * Stack:
 * ..., value1, value2
 * Description:
 * Swap the top two values on the operand stack.
 * The swap instruction must not be used unless value1 and value2
 * are both values of a category 1 computational type (§2.11.1).
 * Notes:
 * The Java Virtual Machine does not provide an instruction
 * implementing a swap on operands of category 2 computational
 * types.
 */
public class SWAP implements Instruction {
    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public int execute(Frame frame) {
        OperandStacks operandStacks = frame.getOperandStacks();

        LocalVariable value1 = operandStacks.pop();
        LocalVariable value2 = operandStacks.pop();

        // push them in reversed order
        operandStacks.push(value1);
        operandStacks.push(value2);

        return frame.getJvmThread().getPc() + this.size();
    }

    @Override
    public int size() {
        return 1;
    }
}
