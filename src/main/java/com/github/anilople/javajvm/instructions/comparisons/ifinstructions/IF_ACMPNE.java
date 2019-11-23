package com.github.anilople.javajvm.instructions.comparisons.ifinstructions;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.utils.ByteUtils;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IF_ACMPNE extends IF implements Instruction {

    private static final Logger logger = LoggerFactory.getLogger(IF_ACMPNE.class);

    @Override
    public int execute(Frame frame) {
        Reference value2 = frame.getOperandStacks().popReference();
        Reference value1 = frame.getOperandStacks().popReference();
        logger.trace("value1: {}, value2: {}", value1, value2);
        if(!value1.equals(value2)) {
            // succeed
            short branchOffset = ByteUtils.bytes2short(this.getBranchbyte1(), this.getBranchbyte2());
            int nextPc = frame.getNextPc() + PrimitiveTypeUtils.intFormSignedShort(branchOffset);
            frame.setNextPc(nextPc);
        } else {
            int nextPc = frame.getNextPc() + this.size();
            frame.setNextPc(nextPc);
        }
        return frame.getJvmThread().getPc() + this.size();
    }

}
