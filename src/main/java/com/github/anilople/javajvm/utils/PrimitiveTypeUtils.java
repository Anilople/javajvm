package com.github.anilople.javajvm.utils;

public class PrimitiveTypeUtils {

    /**
     * unsigned extend
     * @param b
     * @return
     */
    public static int intFormUnsignedByte(byte b) {
        return ((int) b) & 0xFF;
    }

    /**
     * signed extend
     */
    public static int intFormSignedByte(byte b) {
        return (int) b;
    }

    /**
     * unsigned extend
     * @param b
     * @return
     */
    public static short shortFormUnsignedByte(byte b) {
        return (short) intFormUnsignedByte(b);
    }

    /**
     * unsigned extend
     * @param s
     * @return
     */
    public static int intFormUnsignedShort(short s) {
        return 0x0000FFFF & s;
    }

    /**
     * signed extend
     * @param s
     * @return
     */
    public static int intFormSignedShort(short s) {
        return (int) s;
    }
}
