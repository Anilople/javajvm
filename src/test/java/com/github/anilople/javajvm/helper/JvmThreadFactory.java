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

}
