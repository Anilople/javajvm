package com.github.anilople.javajvm.instructions.constants;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

/**
 * Description: Push the long constant <l> (0 or 1) onto the operand stack.
 */
public class LCONST_0 implements Instruction {
    @Override
    public void FetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void Execute(Frame frame) {
        frame.getOperandStacks().pushLongValue(0);
    }
}
