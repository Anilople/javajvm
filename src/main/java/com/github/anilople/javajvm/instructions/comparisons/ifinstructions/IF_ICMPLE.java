package com.github.anilople.javajvm.instructions.comparisons.ifinstructions;

import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

public class IF_ICMPLE extends IF implements Instruction {

    @Override
    public void execute(Frame frame) {
        this.execute(frame, (value1, value2) -> value1 <= value2);
    }

}
