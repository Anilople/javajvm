package com.github.anilople.javajvm.instructions.stack;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.LocalVariable;
import com.github.anilople.javajvm.runtimedataarea.OperandStacks;

/**
 * Why exist this instruction?
 * the answer is that long or double occupies 2 LocalVariable,
 * when we dup an long or a double, we need to dup 2 LocalVariable
 * <p>
 * Operation:
 * Duplicate the top operand stack value and insert two or three
 * values down
 * Description:
 * Duplicate the top value on the operand stack and insert the
 * duplicated value two or three values down in the operand stack.
 */
public class DUP_X2 implements Instruction {
    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public int execute(Frame frame) {
        OperandStacks operandStacks = frame.getOperandStacks();

        // because we have abstract long and double
        // to an abstracted local variable
        // so we don't care about what they use 1 or 2 location in jvm specification
        LocalVariable value1 = operandStacks.pop();
        LocalVariable value2 = operandStacks.pop();

        operandStacks.push(value1);
        operandStacks.push(value2);
        operandStacks.push(value1);

        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
        return frame.getJvmThread().getPc() + this.size();
    }

    @Override
    public int size() {
        return 1;
    }
}
