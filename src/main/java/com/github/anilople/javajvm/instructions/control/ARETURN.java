package com.github.anilople.javajvm.instructions.control;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import com.github.anilople.javajvm.runtimedataarea.Reference;

/**
 * Operation
 * Return reference from method
 *
 * Operand ..., objectref →
 * Stack [empty]
 *
 *
 */
public class ARETURN implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void execute(Frame frame) {
        Reference reference = frame.getOperandStacks().popReference();
        JvmThread jvmThread = frame.getJvmThread();
        // pop current frame
        jvmThread.popFrame();
        // push the objectref to the frame of the invoker
        jvmThread.currentFrame().getOperandStacks().pushReference(reference);
    }

    @Override
    public int size() {
        return 1;
    }

}
