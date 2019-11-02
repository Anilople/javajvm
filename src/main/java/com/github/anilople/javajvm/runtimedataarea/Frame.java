package com.github.anilople.javajvm.runtimedataarea;

import java.util.Stack;

/**
 * A frame is used to store data and partial results, as well as to perform dynamic
 * linking, return values for methods, and dispatch exceptions.
 */
public class Frame {

    private LocalVariables localVariables;

    private OperandStacks operandStacks;

    private Frame() {}

    public Frame(int maxLocals, int maxStack) {
        this.localVariables = new LocalVariables(maxLocals);
        this.operandStacks = new OperandStacks(maxStack);
    }

    public LocalVariables getLocalVariables() {
        return localVariables;
    }

    public OperandStacks getOperandStacks() {
        return operandStacks;
    }
}
