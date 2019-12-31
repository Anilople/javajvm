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

}
