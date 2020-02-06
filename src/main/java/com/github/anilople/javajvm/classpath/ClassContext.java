package com.github.anilople.javajvm.classpath;

/**
 * What a jvm need when it running?
 * One of functions is that read a class file by class name
 * use this interface, jvm can read a class file,
 * and doesn't care about where the class from
 */
interface ClassContext {

    /**
     * given a class name with package path
     * like java/lang/Object
     * like java/lang/String
     * return bytes of this class file
     *
     * @param className class name
     * @return null if there is no this class
     */
    byte[] readClass(String className);
}
