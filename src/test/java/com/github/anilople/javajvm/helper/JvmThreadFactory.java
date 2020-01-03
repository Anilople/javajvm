package com.github.anilople.javajvm.helper;

import com.github.anilople.javajvm.heap.JvmClassLoader;
import com.github.anilople.javajvm.heap.JvmMethod;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import com.github.anilople.javajvm.utils.JvmMethodUtils;

/**
 * make JvmThread
 * @see com.github.anilople.javajvm.runtimedataarea.JvmThread
 */
public class JvmThreadFactory {

    private JvmThreadFactory() {}

    /**
     * from class's "main" method,
     * make a simple thread
     * @param clazz class which exists "main" method
     * @return
     */
    public static JvmThread makeSimpleInstance(Class<?> clazz) {
        // get a simple class loader
        JvmClassLoader jvmClassLoader = JvmClassLoaderFactory.getInstance();
        // get "main" method
        JvmMethod jvmMethod = JvmMethodUtils.getMainMethod(jvmClassLoader, clazz);

        JvmThread jvmThread = new JvmThread();
        // push frame
        jvmThread.pushFrame(new Frame(jvmThread, jvmMethod));

        return jvmThread;
    }

    /**
     * Traditional, we start a thread from "main" method,
     * but for testing, one can start a thread from any static method.
     * @param clazz
     * @param staticMethodName
     * @return
     */
    public static JvmThread createFromStaticMethod(Class<?> clazz, String staticMethodName, String staticMethodDescriptor) {
        // get a simple class loader
        JvmClassLoader jvmClassLoader = JvmClassLoaderFactory.getInstance();

        // get the method given
        JvmMethod jvmMethod = JvmMethodUtils.getJvmMethod(jvmClassLoader, clazz, staticMethodName, staticMethodDescriptor);

        JvmThread jvmThread = new JvmThread();

        // push frame
        jvmThread.pushFrame(new Frame(jvmThread, jvmMethod));

        return jvmThread;
    }
}
