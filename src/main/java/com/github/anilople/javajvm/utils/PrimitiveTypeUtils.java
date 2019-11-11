package com.github.anilople.javajvm.utils;

public class PrimitiveTypeUtils {

    public static int intFormUnsignedByte(byte b) {
        return ((int) b) & 0xFF;
    }

    public static short shortFormUnsignedByte(byte b) {
        return (short) intFormUnsignedByte(b);
    }

    public static int intFormUnsignedShort(short s) {
        return 0x0000FFFF & s;
    }
}
