package com.github.anilople.javajvm.instructions.comparisons;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Operation
 * Compare double
 *
 * Operand ..., value1, value2 →
 * Stack ..., result
 *
 *
 */
public class DCMPG implements Instruction {

    private static final Logger logger = LoggerFactory.getLogger(DCMPG.class);

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void execute(Frame frame) {
        double value2 = frame.getOperandStacks().popDoubleValue();
        double value1 = frame.getOperandStacks().popDoubleValue();
        logger.trace("double compare: [{}] [{}]", value1, value2);
        if(value1 > value2) {
            frame.getOperandStacks().pushIntValue(1);
        } else if(value1 == value2) {
            frame.getOperandStacks().pushIntValue(0);
        } else if(value1 < value2) {
            frame.getOperandStacks().pushIntValue(-1);
        } else {
            logger.debug("at least one is NaN. [{}] [{}]", value1, value2);
            //  at least one of value1' or value2' is NaN
            // The dcmpg instruction pushes the int value 1 onto the operand stack
            frame.getOperandStacks().pushIntValue(1);
        }
        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 1;
    }

}
