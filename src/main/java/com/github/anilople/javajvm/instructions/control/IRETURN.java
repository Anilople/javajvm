package com.github.anilople.javajvm.instructions.control;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import com.github.anilople.javajvm.utils.DescriptorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Operation
 * Return int from method
 *
 * Operand ..., value →
 * Stack [empty]
 *
 * The current method must have return type boolean , byte , short ,
 * char , or int . The value must be of type int .
 *
 * If no exception is thrown, value is popped from the operand
 * stack of the current frame (§2.6) and pushed onto the operand stack
 * of the frame of the invoker.
 *
 * Any other values on the operand stack
 * of the current method are discarded.
 *
 * The interpreter then returns control to the invoker of the method,
 * reinstating the frame of the invoker.
 */
public class IRETURN implements Instruction {

    private static final Logger logger = LoggerFactory.getLogger(IRETURN.class);

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void execute(Frame frame) {
        String returnDescriptor = DescriptorUtils.getReturnDescriptor(frame.getJvmMethod().getDescriptor());
        logger.trace("returnDescriptor: {}", returnDescriptor);
        // check exception, to do...

        // return int
        // get int value
        int intValue = frame.getOperandStacks().popIntValue();
        // pop current frame
        JvmThread jvmThread = frame.getJvmThread();
        jvmThread.popFrame();
        // save int value to frame's stack
        jvmThread.currentFrame().getOperandStacks().pushIntValue(intValue);

    }

    @Override
    public int size() {
        return 1;
    }

}
