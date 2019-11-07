package com.github.anilople.javajvm.runtimedataarea;

import java.util.Stack;

/**
 * Per-thread data areas are created when a thread is
 * created and destroyed when the thread exits.
 */
public class JvmThread {

    /**
     * 2.5.1 The pc Register
     * The Java Virtual Machine can support many threads of execution at once (JLS
     * §17). Each Java Virtual Machine thread has its own pc (program counter) register.
     * At any point, each Java Virtual Machine thread is executing the code of a single
     * method, namely the current method (§2.6) for that thread. If that method is not
     * native , the pc register contains the address of the Java Virtual Machine instruction
     * currently being executed. If the method currently being executed by the thread is
     * native , the value of the Java Virtual Machine's pc register is undefined. The Java
     * Virtual Machine's pc register is wide enough to hold a returnAddress or a native
     * pointer on the specific platform.
     */
    private int pc;

    /**
     * If the computation in a thread requires a larger Java Virtual Machine stack than
     * is permitted, the Java Virtual Machine throws a StackOverflowError
     */
    private int maxStackSize;

    /**
     * Each Java Virtual Machine thread has a private Java Virtual Machine stack, created
     * at the same time as the thread.
     */
    private Stack<Frame> stack;

    public JvmThread() {
        this.pc = 0;
        this.maxStackSize = 1024;
        this.stack = new Stack<>();
    }

    public void pushFrame(Frame frame) {
        if (stack.size() >= maxStackSize) {
            throw new RuntimeException("java.lang.StackOverflowError");
        }
        stack.push(frame);
    }

    public void popFrame() {
        if (stack.size() <= 0) {
            throw new RuntimeException("jvm stack is empty!");
        }
        stack.pop();
    }

    public Frame currentFrame() {
        if (stack.size() <= 0) {
            throw new RuntimeException("jvm stack is empty!");
        }
        return stack.peek();
    }

    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }
}
