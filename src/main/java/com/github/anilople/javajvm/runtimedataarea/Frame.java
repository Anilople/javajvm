package com.github.anilople.javajvm.runtimedataarea;

import com.github.anilople.javajvm.heap.JvmMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A frame is used to store data and partial results, as well as to perform dynamic
 * linking, return values for methods, and dispatch exceptions.
 */
public class Frame {

    private static final Logger logger = LoggerFactory.getLogger(Frame.class);

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

    /**
     * logger level is trace,
     * frame status
     */
    public void traceStatus() {
        logger.trace("jvmThread: {}, jvmMethod: {}", jvmThread, jvmMethod);
        logger.trace("localVariables: {}", localVariables);
        logger.trace("operandStacks: {}", operandStacks);
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
