package com.github.anilople.javajvm.runtimedataarea;

/**
 * A single local variable can hold a value of type boolean , byte , char , short , int ,
 * float , reference , or returnAddress . A pair of local variables can hold a value
 * of type long or double
 */
public class LocalVariable {

    private int intValue;

    private Reference reference;

    public LocalVariable() {
    }

    @Override
    public String toString() {
        return "LocalVariable{" +
                "intValue=" + intValue +
                ", reference=" + reference +
                '}';
    }

    public boolean getBooleanValue() {
        return 0 != intValue;
    }

    public LocalVariable setBooleanValue(boolean booleanValue) {
        this.intValue = booleanValue ? 1 : 0;
        return this;
    }

    public byte getByteValue() {
        return (byte) intValue;
    }

    public LocalVariable setByteValue(byte byteValue) {
        this.intValue = byteValue;
        return this;
    }

    public char getCharValue() {
        return (char) intValue;
    }

    public LocalVariable setCharValue(char charValue) {
        this.intValue = charValue;
        return this;
    }

    public short getShortValue() {
        return (short) intValue;
    }

    public LocalVariable setShortValue(short shortValue) {
        this.intValue = shortValue;
        return this;
    }

    public int getIntValue() {
        return intValue;
    }

    public LocalVariable setIntValue(int intValue) {
        this.intValue = intValue;
        return this;
    }

    public float getFloatValue() {
        return Float.intBitsToFloat(intValue);
    }

    public LocalVariable setFloatValue(float floatValue) {
        this.intValue = Float.floatToRawIntBits(floatValue);
        return this;
    }

    public Reference getReference() {
        return reference;
    }

    public LocalVariable setReference(Reference reference) {
        this.reference = reference;
        return this;
    }

    public int getReturnAddress() {
        return intValue;
    }

    public LocalVariable setReturnAddress(int returnAddress) {
        this.intValue = returnAddress;
        return this;
    }

}
