package com.github.anilople.javajvm.runtimedataarea;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Frame {

    private LocalVariables localVariables;

    private Stack<LocalVariable> operandStacks;

    private Frame() {}

    public Frame(int maxLocals, int maxStack) {
        this.localVariables = new LocalVariables(maxLocals);
        this.operandStacks = new Stack<>();
    }

}
