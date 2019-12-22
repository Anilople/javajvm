package com.github.anilople.javajvm.utils;

import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.heap.JvmClassLoader;
import com.github.anilople.javajvm.heap.JvmMethod;

public class JvmMethodUtils {

    /**
     * get a jvm method by self-define class loader and System-define Class
     * @param jvmClassLoader for load class
     * @param clazz which class this method belong to
     * @param methodName
     * @param methodDescriptor
     * @throws RuntimeException
     * @return
     */
    public static JvmMethod getJvmMethod(
            JvmClassLoader jvmClassLoader,
            Class<?> clazz,
            String methodName,
            String methodDescriptor) {
        JvmClass jvmClass = jvmClassLoader.loadClass(clazz);
        if(!jvmClass.existMethod(methodName, methodDescriptor)) {
            String message = String.format(
                    "class %s does not exists method [name=%s, descriptor=%s]",
                    jvmClass.getName(),
                    methodName,
                    methodDescriptor
            );
            throw new RuntimeException(message);
        }
        return jvmClass.getMethod(methodName, methodDescriptor);
    }

    /**
     * get method
     * public static void main(String[] args)
     * @param jvmClassLoader
     * @param clazz
     * @throws RuntimeException
     * @return
     */
    public static JvmMethod getMainMethod(JvmClassLoader jvmClassLoader, Class<?> clazz) {
        return getJvmMethod(jvmClassLoader, clazz, "main", "([Ljava/lang/String;)V");
    }

}
