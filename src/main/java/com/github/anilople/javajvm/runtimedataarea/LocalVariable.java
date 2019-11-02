package com.github.anilople.javajvm.runtimedataarea;

public class LocalVariable {

    private boolean booleanValue;

    private byte byteValue;

    private char charValue;

    private short shortValue;

    private int intValue;

    private float floatValue;

    private Object reference;

    private int returnAddress;

    private long longValue;

    private double doubleValue;

    public LocalVariable() {}

    public boolean getBooleanValue() {
        return booleanValue;
    }

    public byte getByteValue() {
        return byteValue;
    }

    public char getCharValue() {
        return charValue;
    }

    public short getShortValue() {
        return shortValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public float getFloatValue() {
        return floatValue;
    }

    public Object getReference() {
        return reference;
    }

    public int getReturnAddress() {
        return returnAddress;
    }

    public long getLongValue() {
        return longValue;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public LocalVariable setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
        return this;
    }

    public LocalVariable setByteValue(byte byteValue) {
        this.byteValue = byteValue;
        return this;
    }

    public LocalVariable setCharValue(char charValue) {
        this.charValue = charValue;
        return this;
    }

    public LocalVariable setShortValue(short shortValue) {
        this.shortValue = shortValue;
        return this;
    }

    public LocalVariable setIntValue(int intValue) {
        this.intValue = intValue;
        return this;
    }

    public LocalVariable setFloatValue(float floatValue) {
        this.floatValue = floatValue;
        return this;
    }

    public LocalVariable setReference(Object reference) {
        this.reference = reference;
        return this;
    }

    public LocalVariable setReturnAddress(int returnAddress) {
        this.returnAddress = returnAddress;
        return this;
    }

    public LocalVariable setLongValue(long longValue) {
        this.longValue = longValue;
        return this;
    }

    public LocalVariable setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
        return this;
    }
}
