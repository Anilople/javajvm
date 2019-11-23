package com.github.anilople.javajvm.instructions.comparisons.ifinstructions;

import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

public class IF_ICMPEQ extends IF implements Instruction {

    @Override
    public int execute(Frame frame) {
        this.execute(frame, Integer::equals);
        return frame.getJvmThread().getPc() + this.size();
    }

}
