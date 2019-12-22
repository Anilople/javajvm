package com.github.anilople.javajvm.instructions.control;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Operation
 * Return void from method
 *
 * Operand ... →
 * Stack [empty]
 *
 * Description
 * The current method must have return type void . If the
 * current method is a synchronized method, the monitor entered
 * or reentered on invocation of the method is updated and
 * possibly exited as if by execution of a monitorexit instruction
 * (§monitorexit) in the current thread. If no exception is thrown,
 * any values on the operand stack of the current frame (§2.6) are
 * discarded.
 * The interpreter then returns control to the invoker of the method,
 * reinstating the frame of the invoker.
 */
public class RETURN implements Instruction {

    private static final Logger logger = LoggerFactory.getLogger(RETURN.class);

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void execute(Frame frame) {
        frame.getJvmThread().popFrame();
        logger.trace("pop frame: {}", frame);
        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 1;
    }

}
