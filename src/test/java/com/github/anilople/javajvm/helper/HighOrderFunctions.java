package com.github.anilople.javajvm.helper;

import com.github.anilople.javajvm.heap.JvmClassLoader;
import com.github.anilople.javajvm.heap.JvmMethod;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import com.github.anilople.javajvm.utils.JvmMethodUtils;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * some high order function to make testing more easy
 */
public class HighOrderFunctions {

    /**
     * make a listener just trigger in class's main method
     * @param clazz class, listener will be trigger in this class's main method
     * @param consumer listener which wants to be triggered just in main method
     * @return a new listener
     */
    public static Consumer<JvmThread> toInMainTrigger(Class<?> clazz, Consumer<JvmThread> consumer) {
        return jvmThread -> {
            JvmClassLoader jvmClassLoader = jvmThread.currentFrame().getJvmMethod().getJvmClass().getLoader();
            JvmMethod mainMethod = JvmMethodUtils.getMainMethod(jvmClassLoader, clazz);
            if(jvmThread.currentFrame().getJvmMethod().equals(mainMethod)) {
                consumer.accept(jvmThread);
            }
        };
    }

    /**
     * make a listener just trigger in class's main method
     * @param clazz class, listener will be trigger in this class's main method
     * @param biConsumer istener which wants to be triggered just in main method
     * @return a new listener
     */
    public static BiConsumer<Instruction, JvmThread> toInMainTrigger(
            Class<?> clazz,
            BiConsumer<Instruction, JvmThread> biConsumer) {
        return (instruction, jvmThread) -> {
            JvmClassLoader jvmClassLoader = jvmThread.currentFrame().getJvmMethod().getJvmClass().getLoader();
            JvmMethod mainMethod = JvmMethodUtils.getMainMethod(jvmClassLoader, clazz);
            if(jvmThread.currentFrame().getJvmMethod().equals(mainMethod)) {
                biConsumer.accept(instruction, jvmThread);
            }
        };
    }

}
