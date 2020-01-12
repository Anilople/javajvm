package com.github.anilople.javajvm.instructions.control;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;

/**
 * Operation
 * Return double from method
 *
 * Operand ..., value →
 * Stack [empty]
 *
 *
 */
public class DRETURN implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void execute(Frame frame) {
        double returnValue = frame.getOperandStacks().popDoubleValue();
        JvmThread jvmThread = frame.getJvmThread();
        jvmThread.popFrame();
        jvmThread.currentFrame().getOperandStacks().pushDoubleValue(returnValue);
        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 1;
    }

}
