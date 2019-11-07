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
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public int execute(Frame frame) {
        frame.getOperandStacks().pushReference(Reference.NULL);
        return frame.getJvmThread().getPc() + this.size();
    }

    @Override
    public int size() {
        return 1;
    }
}
