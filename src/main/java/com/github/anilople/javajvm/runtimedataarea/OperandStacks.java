package com.github.anilople.javajvm.runtimedataarea;

import com.github.anilople.javajvm.utils.ByteUtils;

import java.util.Stack;

/**
 * Each frame (§2.6) contains a last-in-first-out (LIFO) stack known as its operand
 * stack. The maximum depth of the operand stack of a frame is determined at
 * compile-time and is supplied along with the code for the method associated with
 * the frame (§4.7.3).
 */
public class OperandStacks {

    private int maxStack;

    private Stack<LocalVariable> operandStack;

    private OperandStacks() {
    }

    public OperandStacks(int maxStack) {
        this.maxStack = maxStack;
        operandStack = new Stack<>();
    }

    @Override
    public String toString() {
        return "OperandStacks{" +
                "maxStack=" + maxStack +
                ", operandStack=" + operandStack +
                '}';
    }

    public void clear() {
        operandStack.clear();
    }

    public void push(LocalVariable localVariable) {
        operandStack.push(localVariable);
    }

    public LocalVariable pop() {
        return operandStack.pop();
    }

    public boolean popBooleanValue() {
        return operandStack.pop().getBooleanValue();
    }

    public byte popByteValue() {
        return operandStack.pop().getByteValue();
    }

    public char popCharValue() {
        return operandStack.pop().getCharValue();
    }

    public short popShortValue() {
        return operandStack.pop().getShortValue();
    }

    public int popIntValue() {
        return operandStack.pop().getIntValue();
    }

    public float popFloatValue() {
        return operandStack.pop().getFloatValue();
    }

    public Reference popReference() {
        return operandStack.pop().getReference();
    }

    public int popReturnAddress() {
        return operandStack.pop().getReturnAddress();
    }

    public long popLongValue() {
        LocalVariable localVariable2 = operandStack.pop();
        LocalVariable localVariable1 = operandStack.pop();
        // pop high bytes
        int intValue2 = localVariable2.getIntValue();
        // pop low bytes
        int intValue1 = localVariable1.getIntValue();
        return ByteUtils.int2long(intValue2, intValue1);
    }

    public double popDoubleValue() {
        return Double.longBitsToDouble(
                this.popLongValue()
        );
    }


    public void pushBooleanValue(boolean booleanValue) {
        operandStack.push(new LocalVariable().setBooleanValue(booleanValue));
    }

    public void pushByteValue(byte byteValue) {
        operandStack.push(new LocalVariable().setByteValue(byteValue));
    }

    public void pushCharValue(char charValue) {
        operandStack.push(new LocalVariable().setCharValue(charValue));
    }

    public void pushShortValue(short shortValue) {
        operandStack.push(new LocalVariable().setShortValue(shortValue));
    }

    public void pushIntValue(int intValue) {
        operandStack.push(new LocalVariable().setIntValue(intValue));
    }

    public void pushFloatValue(float floatValue) {
        operandStack.push(new LocalVariable().setFloatValue(floatValue));
    }

    public void pushReference(Reference reference) {
        operandStack.push(new LocalVariable().setReference(reference));
    }

    public void pushReturnAddress(int returnAddress) {
        operandStack.push(new LocalVariable().setReturnAddress(returnAddress));
    }

    /**
     * a long value occupies 2 local variables
     * push low bytes first
     * then push high bytes
     *
     * @param longValue
     */
    public void pushLongValue(long longValue) {
        // low bytes
        int intValue1 = (int) (longValue);
        // high bytes
        int intValue2 = (int) (longValue >> 32);

        this.pushIntValue(intValue1);
        this.pushIntValue(intValue2);
    }

    public void pushDoubleValue(double doubleValue) {
        this.pushLongValue(
                Double.doubleToLongBits(doubleValue)
        );
    }
}
