package com.github.anilople.javajvm.utils;

import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.heap.JvmClassLoader;
import com.github.anilople.javajvm.heap.JvmMethod;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.LocalVariables;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.BaseTypeArrayReference;
import com.github.anilople.javajvm.runtimedataarea.reference.NullReference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static com.github.anilople.javajvm.constants.Descriptors.BaseType.*;

/**
 * Sometimes for convenience,
 * we use a direct way to implement the function
 */
public class HackUtils {

    private static final Logger logger = LoggerFactory.getLogger(HackUtils.class);

    public static boolean isInHackMethods(JvmMethod jvmMethod) {
        if(jvmMethod.isNative()) {
            logger.warn("Hack judgement for class {} 's native method: {} {}",
                    jvmMethod.getJvmClass().getName(),
                    jvmMethod.getName(),
                    jvmMethod.getDescriptor()
            );
        }
        return jvmMethod.getJvmClass().isSameName(PrintStream.class)
                || (jvmMethod.getJvmClass().isSameName(Class.class) && jvmMethod.isNative());
    }

    /**
     * hack a native method
     * @param frame frame in jvm stack
     * @param jvmMethod the method be hacked
     * @param localVariables args pop from operand stack
     * @return
     */
    public static void hackMethod(Frame frame, JvmMethod jvmMethod, LocalVariables localVariables) {
        if(jvmMethod.getJvmClass().isSameName(PrintStream.class)) {
            // System.out
            hackSystemOut(jvmMethod, localVariables);
        }
        if(jvmMethod.getJvmClass().isSameName(Class.class)) {
            // java.lang.Class
            try {
                hackAllNativeMethod(frame, jvmMethod, localVariables);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                throw new RuntimeException(
                        "hack method " +
                                jvmMethod.getJvmClass().getName() + "." + jvmMethod.getName() +
                                " fail",
                        e
                );
            }
        }
    }

    /**
     * Hack the method in class System.out, i.e PrintStream
     * @see java.io.PrintStream;
     * @param jvmMethod method in runtime
     * @param localVariables variables pop from operand stack by method's parameter descriptors
     */
    public static void hackSystemOut(JvmMethod jvmMethod, LocalVariables localVariables) {
        // the method must belong to class PrintStream
        if(!jvmMethod.getJvmClass().isSameName(PrintStream.class)) {
            return;
        }
        List<String> parameterDescriptors = DescriptorUtils.getParameterDescriptor(jvmMethod.getDescriptor());
        final String methodName = jvmMethod.getName();
        switch (methodName) {
            case "print":
                hackSystemOutPrint(localVariables, parameterDescriptors.get(0));
                break;
            case "println":
                if(parameterDescriptors.size() > 0) {
                    hackSystemOutPrint(localVariables, parameterDescriptors.get(0));
                }
                System.out.println();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + methodName);
        }
    }

    /**
     * Hack the method "print" in PrintStream
     * @see java.io.PrintStream;
     * @param localVariables variables pop from operand stack by method's parameter descriptors
     * @param parameterDescriptor method's parameter descriptor, just 1
     */
    private static void hackSystemOutPrint(LocalVariables localVariables, String parameterDescriptor) {
        switch (parameterDescriptor) {
            case BYTE: {
                byte value = localVariables.getByteValue(1);
                System.out.print(value);
                break;
            }
            case CHAR: {
                char value = localVariables.getCharValue(1);
                System.out.print(value);
                break;
            }
            case DOUBLE: {
                double value = localVariables.getDoubleValue(1);
                System.out.print(value);
                break;
            }
            case FLOAT: {
                float value = localVariables.getFloatValue(1);
                System.out.print(value);
                break;
            }
            case INT: {
                int value = localVariables.getIntValue(1);
                System.out.print(value);
                break;
            }
            case LONG: {
                long value = localVariables.getLongValue(1);
                System.out.print(value);
                break;
            }
            case SHORT: {
                short value = localVariables.getShortValue(1);
                System.out.print(value);
                break;
            }
            case BOOLEAN: {
                boolean value = localVariables.getBooleanValue(1);
                System.out.print(value);
                break;
            }
            case "Ljava/lang/String;": {
                Reference reference = localVariables.getReference(1);
                if(reference instanceof NullReference) {
                    System.out.print("null");
                } else {
                    // string reference
                    ObjectReference objectReference = (ObjectReference) reference;
                    // get the char array
                    BaseTypeArrayReference baseTypeArrayReference = (BaseTypeArrayReference) objectReference.getReference(0);
                    char[] chars = new char[baseTypeArrayReference.length()];
                    // copy
                    for(int i = 0; i < baseTypeArrayReference.length(); i++) {
                        chars[i] = baseTypeArrayReference.getCharValue(i);
                    }
                    // copy finished
                    System.out.print(chars);
                }
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + parameterDescriptor);
        }
    }

    public static void assertNativeMethod(JvmMethod jvmMethod) {
        if(!jvmMethod.isNative()) {
            throw new RuntimeException(jvmMethod.getName() + jvmMethod.getDescriptor() + " must be native method");
        }
    }

    /**
     * hack the native method
     * i.e when our JVM wants to invoke a native method,
     * we intercept the invocation,
     * then hack it.
     * invoke the method by reflection to get the result
     * @param frame
     * @param jvmMethod
     * @param localVariables
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static void hackAllNativeMethod(
            Frame frame, JvmMethod jvmMethod, LocalVariables localVariables
    ) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        assertNativeMethod(jvmMethod);
        final JvmClass jvmClass = jvmMethod.getJvmClass();
        final JvmClassLoader jvmClassLoader = jvmClass.getLoader();
        final String methodName = jvmMethod.getName();
        final Class<?> clazz = jvmClass.getRealClassInJvm();
        // parse type of parameters
        Class<?>[] parameterTypes = DescriptorUtils.methodDescriptor2ParameterTypes(jvmMethod.getDescriptor());
        // find the method
        final Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);

        int parameterOffset = jvmMethod.isStatic() ? 0 : 1;
        // parameters pass to method when invoke
        Object[] parameterObjects = new Object[parameterTypes.length];
        for(int i = 0; i < parameterObjects.length; i++) {
            parameterObjects[i] = ReferenceUtils.getLocalVariableByClassType(localVariables, parameterOffset, parameterTypes[i]);
            parameterOffset += ReflectionUtils.getClassSize(parameterTypes[i]);
        }

        // the result of invocation
        Object returnObject = null;
        if(jvmMethod.isStatic()) {
            returnObject = method.invoke(null, parameterObjects);
        } else {
            // non static method, so we must pass this pointer
            Reference reference = localVariables.getReference(0);
            Object thisObject = ReferenceUtils.reference2Object(reference);
            returnObject = method.invoke(thisObject, parameterObjects);
        }

        if(!void.class.equals(method.getReturnType())) {
            // exists return value
            Reference returnReference = ReferenceUtils.object2Reference(jvmClassLoader, returnObject);
            // push the result
            frame.getOperandStacks().pushReference(returnReference);
        }
    }

}
