package com.github.anilople.javajvm.instructions.stack;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

/**
 * pop2 instruction is used to pop the value
 * from operand stack which may occupies
 * 2 consecutive local variables(like long and double)
 * <p>
 * Operation
 * Pop the top one or two operand stack values
 * Description:
 * Pop the top one or two values from the operand stack.
 */
public class POP2 implements Instruction {
    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    /**
     * What will happen if the value is long or double?
     * Don't worry, we implement them by an abstracted
     * LocalVariable, so just pop 1 LocalVariable
     *
     * @param frame
     */
    @Override
    public int execute(Frame frame) {
        frame.getOperandStacks().pop();
        return frame.getJvmThread().getPc() + this.size();
    }

    @Override
    public int size() {
        return 1;
    }
}
