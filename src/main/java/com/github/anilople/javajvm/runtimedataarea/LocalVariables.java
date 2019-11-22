package com.github.anilople.javajvm.runtimedataarea;

import com.github.anilople.javajvm.utils.ByteUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Each frame (§2.6) contains an array of variables known as its local variables. The
 * length of the local variable array of a frame is determined at compile-time and
 * supplied in the binary representation of a class or interface along with the code for
 * the method associated with the frame (§4.7.3).
 * <p>
 * A value of type long or type double occupies two consecutive local variables.
 * Such a value may only be addressed using the lesser index. For example, a value of
 * type double stored in the local variable array at index n actually occupies the local
 * variables with indices n and n+1; however, the local variable at index n+1 cannot
 * be loaded from. It can be stored into. However, doing so invalidates the contents
 * of local variable n.
 */
public class LocalVariables {

    private int maxLocals;

    private List<LocalVariable> localVariables;

    private LocalVariables() {
    }

    public LocalVariables(int maxLocals) {
        this.maxLocals = maxLocals;
        this.localVariables = Arrays.asList(new LocalVariable[maxLocals]);
    }

    @Override
    public String toString() {
        return "LocalVariables{" +
                "maxLocals=" + maxLocals +
                ", localVariables=" + localVariables +
                '}';
    }

    public boolean getBooleanValue(int index) {
        return localVariables.get(index).getBooleanValue();
    }

    public byte getByteValue(int index) {
        return localVariables.get(index).getByteValue();
    }

    public char getCharValue(int index) {
        return localVariables.get(index).getCharValue();
    }

    public short getShortValue(int index) {
        return localVariables.get(index).getShortValue();
    }

    public int getIntValue(int index) {
        return localVariables.get(index).getIntValue();
    }

    public float getFloatValue(int index) {
        return localVariables.get(index).getFloatValue();
    }

    public Reference getReference(int index) {
        return localVariables.get(index).getReference();
    }

    public int getReturnAddress(int index) {
        return localVariables.get(index).getReturnAddress();
    }

    public long getLongValue(int index) {
        int value0 = localVariables.get(index).getIntValue();
        int value1 = localVariables.get(index + 1).getIntValue();
        return ByteUtils.int2long(value0, value1);
    }

    public double getDoubleValue(int index) {
        long longValue = this.getLongValue(index);
        return Double.longBitsToDouble(longValue);
    }

    public void setBooleanValue(int index, boolean booleanValue) {
        localVariables.set(index, new LocalVariable().setBooleanValue(booleanValue));
    }

    public void setByteValue(int index, byte byteValue) {
        localVariables.set(index, new LocalVariable().setByteValue(byteValue));
    }

    public void setCharValue(int index, char charValue) {
        localVariables.set(index, new LocalVariable().setCharValue(charValue));
    }

    public void setShortValue(int index, short shortValue) {
        localVariables.set(index, new LocalVariable().setShortValue(shortValue));
    }

    public void setIntValue(int index, int intValue) {
        localVariables.set(index, new LocalVariable().setIntValue(intValue));
    }

    public void setFloatValue(int index, float floatValue) {
        localVariables.set(index, new LocalVariable().setFloatValue(floatValue));
    }

    public void setReference(int index, Reference reference) {
        localVariables.set(index, new LocalVariable().setReference(reference));
    }

    public void setReturnAddress(int index, int returnAddress) {
        localVariables.set(index, new LocalVariable().setReturnAddress(returnAddress));
    }

    public void setLongValue(int index, long longValue) {
        // high bytes
        int value0 = (int) (longValue >> 32);
        int value1 = (int) (longValue);
        localVariables.set(index, new LocalVariable().setIntValue(value0));
        localVariables.set(index + 1, new LocalVariable().setIntValue(value1));
    }

    public void setDoubleValue(int index, double doubleValue) {
        long longValue = Double.doubleToLongBits(doubleValue);
        this.setLongValue(index, longValue);
    }

}
