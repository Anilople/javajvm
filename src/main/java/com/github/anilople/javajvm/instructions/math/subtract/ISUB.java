package com.github.anilople.javajvm.instructions.math.subtract;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

/**
 * Operation
 * Subtract int
 *
 * Operand ..., value1, value2 →
 * Stack ..., result
 *
 * Description
 * Both value1 and value2 must be of type int . The values are popped
 * from the operand stack. The int result is value1 - value2. The
 * result is pushed onto the operand stack.
 *
 * Despite the fact that overflow may occur, execution of an isub
 * instruction never throws a run-time exception.
 */
public class ISUB implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public int execute(Frame frame) {
        int value2 = frame.getOperandStacks().popIntValue();
        int value1 = frame.getOperandStacks().popIntValue();
        int result = value1 - value2;
        frame.getOperandStacks().pushIntValue(result);

        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
        return frame.getJvmThread().getPc() + this.size();
    }

    @Override
    public int size() {
        return 1;
    }

}
