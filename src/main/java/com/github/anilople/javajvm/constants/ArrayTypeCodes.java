package com.github.anilople.javajvm.constants;

/**
 * Table 6.5.newarray-A. Array type codes
 * Array Type   atype
 * T_BOOLEAN    4
 * T_CHAR       5
 * T_FLOAT      6
 * T_DOUBLE     7
 * T_BYTE       8
 * T_SHORT      9
 * T_INT        10
 * T_LONG       11
 */
public class ArrayTypeCodes {

    public static final byte T_BOOLEAN = 4;
    public static final byte T_CHAR = 5;
    public static final byte T_FLOAT = 6;
    public static final byte T_DOUBLE = 7;
    public static final byte T_BYTE = 8;
    public static final byte T_SHORT = 9;
    public static final byte T_INT = 10;
    public static final byte T_LONG = 11;

    /**
     *
     * @param descriptor base type descriptor
     * @return type code of base type array
     */
    public static byte fromDescriptor(String descriptor) {
        switch (descriptor) {
            case Descriptors.BaseType.BOOLEAN:
                return T_BOOLEAN;
            case Descriptors.BaseType.CHAR:
                return T_CHAR;
            case Descriptors.BaseType.FLOAT:
                return T_FLOAT;
            case Descriptors.BaseType.DOUBLE:
                return T_DOUBLE;
            case Descriptors.BaseType.BYTE:
                return T_BYTE;
            case Descriptors.BaseType.SHORT:
                return T_SHORT;
            case Descriptors.BaseType.INT:
                return T_INT;
            case Descriptors.BaseType.LONG:
                return T_LONG;
            default:
                throw new IllegalStateException("Unexpected value: " + descriptor);
        }
    }

    /**
     * java.lang.Class -> byte code
     * @param clazz
     * @return
     */
    public static byte fromClass(Class<?> clazz) {
        final Class<?> type = clazz;
        if(type.equals(boolean.class)) {
            return T_BOOLEAN;
        } else if(type.equals(byte.class)) {
            return T_BYTE;
        } else if(type.equals(short.class)) {
            return T_SHORT;
        } else if(type.equals(char.class)) {
            return T_CHAR;
        } else if(type.equals(int.class)) {
            return T_INT;
        } else if(type.equals(float.class)) {
            return T_FLOAT;
        } else if(type.equals(long.class)) {
            return T_LONG;
        } else if(type.equals(double.class)) {
            return T_DOUBLE;
        } else {
            throw new IllegalArgumentException("Cannot set type " + type);
        }
    }

    /**
     * type code -> java.lang.Class
     * @param typeCode
     * @return
     */
    public static Class<?> typeCode2PrimitiveClass(byte typeCode) {
        switch (typeCode) {
            case ArrayTypeCodes.T_BOOLEAN:
                return boolean.class;
            case ArrayTypeCodes.T_BYTE:
                return byte.class;
            case ArrayTypeCodes.T_CHAR:
                return char.class;
            case ArrayTypeCodes.T_SHORT:
                return short.class;
            case ArrayTypeCodes.T_INT:
                return int.class;
            case ArrayTypeCodes.T_LONG:
                return long.class;
            case ArrayTypeCodes.T_FLOAT:
                return float.class;
            case ArrayTypeCodes.T_DOUBLE:
                return double.class;
            default:
                throw new IllegalStateException("Unexpected value: " + typeCode);
        }
    }
}
