package com.github.anilople.javajvm.runtimedataarea;

import com.github.anilople.javajvm.classfile.ClassFile;

/**
 * A frame is used to store data and partial results, as well as to perform dynamic
 * linking, return values for methods, and dispatch exceptions.
 */
public class Frame {

    private ClassFile classFile;

    private JvmThread jvmThread;

    private LocalVariables localVariables;

    private OperandStacks operandStacks;

    private Frame() {
    }

    public Frame(ClassFile classFile, JvmThread jvmThread, int maxLocals, int maxStack) {
        this.classFile = classFile;
        this.jvmThread = jvmThread;
        this.localVariables = new LocalVariables(maxLocals);
        this.operandStacks = new OperandStacks(maxStack);
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
}
