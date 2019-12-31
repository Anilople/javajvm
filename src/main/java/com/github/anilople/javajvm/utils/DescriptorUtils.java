package com.github.anilople.javajvm.utils;

import com.github.anilople.javajvm.constants.Descriptors;
import com.github.anilople.javajvm.runtimedataarea.LocalVariables;
import com.github.anilople.javajvm.runtimedataarea.OperandStacks;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
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
        logger.trace("parameterDescriptor: {}", parameterDescriptor);
        String returnDescriptor = descriptor.substring(descriptor.lastIndexOf(')') + 1);
        logger.trace("returnDescriptor: {}", returnDescriptor);
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
     * ObjectType:
     *      L ClassName ;
     * get the ClassName between 'L' and ';'
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

    /**
     * @param fieldDescriptor must be FieldDescriptor
     * @throws RuntimeException
     * @return
     */
    public static int getFieldDescriptorSize(String fieldDescriptor) {
        if(!DescriptorUtils.isFieldDescriptor(fieldDescriptor)) {
            throw new RuntimeException(fieldDescriptor + " is not FieldDescriptor");
        }

        switch (fieldDescriptor) {
            case Descriptors.BaseType.LONG:
            case Descriptors.BaseType.DOUBLE:
                return 2;
            default:
                return 1;
        }
    }

    /**
     * for the max local in local variables
     * @param parameterDescriptors must be ParameterDescriptor
     * @throws RuntimeException
     * @return
     */
    public static int getParameterDescriptorsSize(List<String> parameterDescriptors) {
        int sum = 0;
        for(String parameterDescriptor : parameterDescriptors) {
            sum += DescriptorUtils.getFieldDescriptorSize(parameterDescriptor);
        }
        return sum;
    }

    /**
     * Operand ..., objectref, [arg1, [arg2 ...]] →
     * Stack
     *
     * objectref in local variable 0, arg1 in local variable 1 (or, if arg1 is of
     * type long or double , in local variables 1 and 2), and so on.
     *
     * pop value in stack according to parameter descriptor
     * the values pop from stack will save in LocalVariables reversed
     *
     *
     * Example:
     *      static invoke(without object reference)
     *          (ILjava/lang/Object;) :
     *              pop Reference, pop int,
     *              but in local variables,
     *              the values are [I, Ljava/lang/Object;]
     *      non static invoke:
     *          (J[I):
     *              pop int array, pop long, pop object reference
     *              but in local variables
     *              the values are [objectref, long, int array]
     *
     * @param existsObjectReference distinguish that there an Object Reference or not(max locals in local variables)
     * @param operandStacks
     * @param parameterDescriptors
     * @throws RuntimeException
     * @return
     */
    public static LocalVariables popArgsByParameterDescriptor(boolean existsObjectReference, OperandStacks operandStacks, List<String> parameterDescriptors) {
        int parameterDescriptorsSize = DescriptorUtils.getParameterDescriptorsSize(parameterDescriptors);
        LocalVariables localVariables
                = existsObjectReference ?
                new LocalVariables(parameterDescriptorsSize + 1)
                :
                new LocalVariables(parameterDescriptorsSize)
                ;

        // reverse parameter descriptors list for pop value
        Collections.reverse(parameterDescriptors);

        // start index
        int localVariableIndex = 0;
        for(String parameterDescriptor : parameterDescriptors) {
            logger.trace("one parameterDescriptor: {}", parameterDescriptor);
            if(DescriptorUtils.isBaseType(parameterDescriptor)) {
                switch (parameterDescriptor) {
                    case Descriptors.BaseType.BOOLEAN:
                    case Descriptors.BaseType.BYTE:
                    case Descriptors.BaseType.CHAR:
                    case Descriptors.BaseType.SHORT:
                    case Descriptors.BaseType.INT:
                        int intValue = operandStacks.popIntValue();
                        localVariables.setIntValue(localVariableIndex, intValue);
                        break;
                    case Descriptors.BaseType.FLOAT:
                        float floatValue = operandStacks.popFloatValue();
                        localVariables.setFloatValue(localVariableIndex, floatValue);
                        break;
                    case Descriptors.BaseType.LONG:
                        long longValue = operandStacks.popLongValue();
                        localVariables.setLongValue(localVariableIndex, longValue);
                        localVariableIndex += 1;
                        break;
                    case Descriptors.BaseType.DOUBLE:
                        double doubleValue = operandStacks.popDoubleValue();
                        localVariables.setDoubleValue(localVariableIndex, doubleValue);
                        localVariableIndex += 1;
                }
                localVariableIndex += 1;
            } else if(DescriptorUtils.isObjectType(parameterDescriptor)) {
                Reference reference = operandStacks.popReference();
                localVariables.setReference(localVariableIndex, reference);
                localVariableIndex += 1;
            } else if(DescriptorUtils.isArrayType(parameterDescriptor)) {
                throw new RuntimeException(parameterDescriptor + " array type not support now.");
            } else {
                throw new RuntimeException("What descriptor is " + parameterDescriptor);
            }
        }

        // maybe pop object reference
        if(existsObjectReference) {
            Reference reference = operandStacks.popReference();
            localVariables.setReference(localVariableIndex, reference);
            localVariableIndex += 1;
        }

        // now in local variables, [..., arg2, arg1, {objectref}]
        // so we need to reverse it
        localVariables.reverse();
        return localVariables;
    }

    /**
     * ArrayType:
     *      [ ComponentType
     * @param arrayClassDescriptor
     * @return ComponentType
     */
    public static String getComponentType(String arrayClassDescriptor) {
        return arrayClassDescriptor.substring(1);
    }

    /**
     * I -> 0
     * [I -> 1
     * [[I -> 2
     * ...
     * @param arrayClassDescriptor
     * @return the dimensions of array
     */
    public static int getDimensions(String arrayClassDescriptor) {
        int dimensions = 0;
        for(char c : arrayClassDescriptor.toCharArray()) {
            if('[' == c) {
                dimensions += 1;
            } else {
                break;
            }
        }
        return dimensions;
    }
}
