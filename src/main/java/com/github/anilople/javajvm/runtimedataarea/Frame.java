package com.github.anilople.javajvm.runtimedataarea;

import com.github.anilople.javajvm.heap.JvmMethod;

/**
 * A frame is used to store data and partial results, as well as to perform dynamic
 * linking, return values for methods, and dispatch exceptions.
 */
public class Frame {

    private JvmThread jvmThread;

    private LocalVariables localVariables;

    private OperandStacks operandStacks;

    private JvmMethod jvmMethod;

    private Frame() {
    }

    public Frame(JvmThread jvmThread, JvmMethod jvmMethod) {
        this.jvmThread = jvmThread;
        this.localVariables = new LocalVariables(jvmMethod.getMaxLocals());
        this.operandStacks = new OperandStacks(jvmMethod.getMaxStack());
        this.jvmMethod = jvmMethod;
    }

    public JvmThread getJvmThread() {
        return jvmThread;
    }

    public LocalVariables getLocalVariables() {
        return localVariables;
    }

    public OperandStacks getOperandStacks() {
        return operandStacks;
    }

    public JvmMethod getJvmMethod() {
        return jvmMethod;
    }
}
