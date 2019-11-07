package com.github.anilople.javajvm.instructions.constants;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

/**
 * Description: Push the float constant <f> (0.0, 1.0, or 2.0) onto the operand
 * stack.
 */
public class FCONST_1 implements Instruction {
    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public int execute(Frame frame) {
        frame.getOperandStacks().pushFloatValue(1.0F);
        return this.size();
    }

    @Override
    public int size() {
        return 1;
    }
}
