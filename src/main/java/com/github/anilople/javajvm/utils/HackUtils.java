package com.github.anilople.javajvm.utils;

import com.github.anilople.javajvm.heap.JvmMethod;
import com.github.anilople.javajvm.runtimedataarea.LocalVariables;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.BaseTypeArrayReference;
import com.github.anilople.javajvm.runtimedataarea.reference.NullReference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
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
        return jvmMethod.getJvmClass().isSameName(PrintStream.class);
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

}
