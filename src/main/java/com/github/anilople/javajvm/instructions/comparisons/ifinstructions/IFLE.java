package com.github.anilople.javajvm.instructions.comparisons.ifinstructions;

import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

public class IFLE extends IF implements Instruction {

    @Override
    public void execute(Frame frame) {
        this.execute(frame, value -> value <= 0);
    }

}
