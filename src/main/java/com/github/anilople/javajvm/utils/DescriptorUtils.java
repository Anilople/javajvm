package com.github.anilople.javajvm.utils;

import com.github.anilople.javajvm.constants.Descriptors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

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

    private static final Logger logger = LoggerFactory.getLogger(DescriptorUtils.class);

    /**
     * FieldDescriptor:
     *      FieldType
     * @param descriptor
     * @return
     */
    public static boolean isFieldDescriptor(String descriptor) {
        return isFieldType(descriptor);
    }

    /**
     * FieldType:
     *      BaseType
     *      ObjectType
     *      ArrayType
     * @param descriptor
     * @return
     */
    public static boolean isFieldType(String descriptor) {
        return isBaseType(descriptor) || isObjectType(descriptor) || isArrayType(descriptor);
    }

    /**
     * BaseType:
     *      (one of)
     *      B C D F I J S Z
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
     *      L ClassName ;
     * @param descriptor
     * @return
     */
    public static boolean isObjectType(String descriptor) {
        return descriptor.startsWith("L")
                && descriptor.endsWith(";")
                && isClassName(descriptor.substring(1, descriptor.length() - 1));
    }

    /**
     * judge string is a valid class name or not
     * @param className
     * @return
     */
    public static boolean isClassName(String className) {
        return true;
    }


    /**
     * ArrayType:
     *      [ ComponentType
     * @param descriptor
     * @return
     */
    public static boolean isArrayType(String descriptor) {
        return descriptor.startsWith("[")
                && isComponentType(descriptor.substring(1));
    }

    /**
     * ComponentType:
     *      FieldType
     * @param componentType
     * @return
     */
    public static boolean isComponentType(String componentType) {
        return isFieldType(componentType);
    }


    /**
     * get the content in first Parentheses pair
     * suppose first char is '('
     * @param descriptor
     * @return
     */
    private static String getInFirstParentheses(String descriptor) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 1; i < descriptor.length() && (')' != descriptor.charAt(i)); i++) {
            stringBuilder.append(descriptor.charAt(i));
        }
        return stringBuilder.toString();
    }

    /**
     * MethodDescriptor:
     *      ( {ParameterDescriptor} ) ReturnDescriptor
     * @param descriptor
     * @return
     */
    public static boolean isMethodDescriptor(String descriptor) {
        String parameterDescriptor =  getInFirstParentheses(descriptor);
        String returnDescriptor = descriptor.substring(descriptor.lastIndexOf(')') + 1);
        return (parameterDescriptor.length() <= 0 || isParameterDescriptor(parameterDescriptor))
                && isReturnDescriptor(returnDescriptor);
    }

    /**
     * ParameterDescriptor:
     *      FieldType
     * @param parameterDescriptor
     * @return
     */
    public static boolean isParameterDescriptor(String parameterDescriptor) {
        return isFieldType(parameterDescriptor);
    }

    /**
     * ReturnDescriptor:
     *      FieldType
     *      VoidDescriptor
     * @param returnDescriptor
     * @return
     */
    public static boolean isReturnDescriptor(String returnDescriptor) {
        return isFieldType(returnDescriptor) || isVoidDescriptor(returnDescriptor);
    }

    public static boolean isVoidDescriptor(String voidDescriptor) {
        return voidDescriptor.equals("V");
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
     * MethodDescriptor:
     *      ( {ParameterDescriptor} ) ReturnDescriptor
     * @param methodDescriptor
     * @return
     */
    public static List<String> getParameterDescriptor(String methodDescriptor) {
        logger.trace("methodDescriptor: {}", methodDescriptor);
        List<String> parameterDescriptors = new ArrayList<>();

        String parameterDescriptor =  getInFirstParentheses(methodDescriptor);
        logger.trace("parameterDescriptor: {}", parameterDescriptor);
        for(int i = 0; i < parameterDescriptor.length(); i++) {
            if('L' == parameterDescriptor.charAt(i)) {
                // object reference
                logger.trace("object reference i = {}", i);
                // find character ';', dangerous! for loop
                int semicolonIndex = -1;
                for(semicolonIndex = i + 1;
                    semicolonIndex < parameterDescriptor.length() && ';' != parameterDescriptor.charAt(semicolonIndex);
                    semicolonIndex++) {

                }
                logger.trace("; semicolonIndex = {}", semicolonIndex);
                String objectReferenceDescriptor = parameterDescriptor.substring(i, semicolonIndex + 1);
                logger.trace("objectReferenceDescriptor: {}", objectReferenceDescriptor);
                parameterDescriptors.add(objectReferenceDescriptor);
                // update i
                i = semicolonIndex + 1;
            } else if('[' == parameterDescriptor.charAt(i)) {
                // array reference
                logger.trace("array reference i = {}", i);
                throw new RuntimeException("no support array reference now");
            } else {
                // base type
                logger.trace("base type i = {}, char = {}", i, parameterDescriptor.charAt(i));
                parameterDescriptors.add(String.valueOf(parameterDescriptor.charAt(i)));
            }
        }

        return parameterDescriptors;
    }

    /**
     * MethodDescriptor:
     *      ( {ParameterDescriptor} ) ReturnDescriptor
     * @param methodDescriptor
     * @return ReturnDescriptor
     */
    public static String getReturnDescriptor(String methodDescriptor) {
        int rightParenthesesIndex = methodDescriptor.indexOf(")");
        return methodDescriptor.substring(rightParenthesesIndex + 1, methodDescriptor.length());
    }
}
