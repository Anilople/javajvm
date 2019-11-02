package com.github.anilople.javajvm.runtimedataarea;

import java.util.Stack;

public class Thread {

    private int pc;

    private int maxStackSize;

    private Stack<Frame> stack;

    public Thread() {
        this.pc = 0;
        this.maxStackSize = 1024;
        this.stack = new Stack<>();
    }

    public void pushFrame(Frame frame) {
        if(stack.size() >= maxStackSize) {
            throw new RuntimeException("java.lang.StackOverflowError");
        }
        stack.push(frame);
    }

    public void popFrame() {
        if(stack.size() <= 0) {
            throw new RuntimeException("jvm stack is empty!");
        }
        stack.pop();
    }

    public Frame currentFrame() {
        if(stack.size() <= 0) {
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
