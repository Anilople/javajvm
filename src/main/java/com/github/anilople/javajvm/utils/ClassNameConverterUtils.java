package com.github.anilople.javajvm.utils;

/**
 * a class exists 2 different names between Java level and JVM level
 *
 * Example:
 *      Java level          JVM level
 *      java.lang.Object    java/lang/Object
 *      java.lang.String    java/lang/String
 */
public class ClassNameConverterUtils {

    /**
     * java/lang/Object -> java.lang.Object
     * @param jvmClassName
     * @return
     */
    public static String jvm2java(String jvmClassName) {
        return jvmClassName.replace('/', '.');
    }

    /**
     * java.lang.Object -> java/lang/Object
     * @param javaClassName
     * @return
     */
    public static String java2jvm(String javaClassName) {
        return javaClassName.replace('.', '/');
    }

    public static String java2jvm(Class<?> clazz) {
        return java2jvm(clazz.getName());
    }
}
