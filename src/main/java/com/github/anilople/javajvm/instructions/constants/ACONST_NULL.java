package com.github.anilople.javajvm.instructions.constants;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.Reference;

/**
 * Description: Push the null object reference onto the operand stack.
 * Notes: The Java Virtual Machine does not mandate a concrete value for null
 */
public class ACONST_NULL implements Instruction {

    @Override
    public void FetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void Execute(Frame frame) {
        frame.getOperandStacks().pushReference(Reference.NULL);
    }
}
