package com.github.anilople.javajvm.helper;

import com.github.anilople.javajvm.runtimedataarea.OperandStacks;
import com.github.anilople.javajvm.runtimedataarea.Reference;

import java.util.function.Consumer;

/**
 * without stack's top method,
 * sometimes, we just need pop the value on the top of stack,
 * check it,
 * then push it back,
 * this class supplies the method which abstract the action
 */
public class OperandStacksHelper {

    public static void checkTopBoolean(OperandStacks operandStacks, Consumer<Boolean> consumer) {
        boolean value = operandStacks.popBooleanValue();
        consumer.accept(value);
        operandStacks.pushBooleanValue(value);
    }

    public static void checkTopByte(OperandStacks operandStacks, Consumer<Byte> consumer) {
        byte value = operandStacks.popByteValue();
        consumer.accept(value);
        operandStacks.pushByteValue(value);
    }

    public static void checkTopShort(OperandStacks operandStacks, Consumer<Short> consumer) {
        short value = operandStacks.popShortValue();
        consumer.accept(value);
        operandStacks.pushShortValue(value);
    }

    public static void checkTopInt(OperandStacks operandStacks, Consumer<Integer> consumer) {
        int value = operandStacks.popIntValue();
        consumer.accept(value);
        operandStacks.pushIntValue(value);
    }

    public static void checkTopLong(OperandStacks operandStacks, Consumer<Long> consumer) {
        long value = operandStacks.popLongValue();
        consumer.accept(value);
        operandStacks.pushLongValue(value);
    }

    public static void checkTopFloat(OperandStacks operandStacks, Consumer<Float> consumer) {
        float value = operandStacks.popFloatValue();
        consumer.accept(value);
        operandStacks.pushFloatValue(value);
    }

    public static void checkTopDouble(OperandStacks operandStacks, Consumer<Double> consumer) {
        double value = operandStacks.popDoubleValue();
        consumer.accept(value);
        operandStacks.pushDoubleValue(value);
    }

    public static void checkTopReference(OperandStacks operandStacks, Consumer<Reference> consumer) {
        Reference reference = operandStacks.popReference();
        consumer.accept(reference);
        operandStacks.pushReference(reference);
    }
}
