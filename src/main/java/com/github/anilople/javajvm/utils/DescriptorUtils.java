package com.github.anilople.javajvm.utils;

import com.github.anilople.javajvm.constants.Descriptors;

/**
 * some tools for descriptor
 *
 * 4.3.2 Field Descriptors
 *
 * FieldDescriptor:
 *      FieldType
 * FieldType:
 *      BaseType
 *      ObjectType
 *      ArrayType
 * BaseType:
 *      (one of)
 *      B C D F I J S Z
 * ObjectType:
 *      L ClassName ;
 * ArrayType:
 *      [ ComponentType
 * ComponentType:
 *      FieldType
 *
 * 4.3.3 Method Descriptors
 *
 * MethodDescriptor:
 *      ( {ParameterDescriptor} ) ReturnDescriptor
 * ParameterDescriptor:
 *      FieldType
 * ReturnDescriptor:
 *      FieldType
 *      VoidDescriptor
 * VoidDescriptor:
 *      V
 */
public class DescriptorUtils {

    /**
     * BaseType:
     *  (one of)
     *  B C D F I J S Z
     * @param descriptor
     * @return
     */
    public static boolean isBaseType(String descriptor) {
        return descriptor.equals(Descriptors.BaseType.BOOLEAN)
                || descriptor.equals(Descriptors.BaseType.BYTE)
                || descriptor.equals(Descriptors.BaseType.CHAR)
                || descriptor.equals(Descriptors.BaseType.SHORT)
                || descriptor.equals(Descriptors.BaseType.INT)
                || descriptor.equals(Descriptors.BaseType.LONG)
                || descriptor.equals(Descriptors.BaseType.FLOAT)
                || descriptor.equals(Descriptors.BaseType.DOUBLE);
    }

    /**
     * ObjectType:
     *  L ClassName ;
     * @param descriptor
     * @return
     */
    public static boolean isObjectType(String descriptor) {
        return descriptor.startsWith("L") && descriptor.endsWith(";");
    }

    /**
     * suppose that descriptor is ObjectType
     * @param descriptor
     * @return class name
     */
    public static String getClassName(String descriptor) {
        return descriptor.substring(1, descriptor.length() - 1);
    }

    /**
     * to do
     * @param descriptor
     * @return
     */
    public static boolean isArrayType(String descriptor) {
        return descriptor.startsWith("[");
    }

}
