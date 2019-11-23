package com.github.anilople.javajvm.instructions.comparisons.ifinstructions;

import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

public class IFGE extends IF implements Instruction {

    @Override
    public int execute(Frame frame) {
        this.execute(frame, value -> value >= 0);
        return frame.getJvmThread().getPc() + this.size();
    }

}
