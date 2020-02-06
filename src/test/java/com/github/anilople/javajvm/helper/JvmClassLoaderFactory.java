package com.github.anilople.javajvm.helper;

import com.github.anilople.javajvm.classpath.Classpath;
import com.github.anilople.javajvm.command.Command;
import com.github.anilople.javajvm.heap.JvmClassLoader;

/**
 * When write a testing,
 * we need a runtime class loader to load some classes
 */
public class JvmClassLoaderFactory {

    public static JvmClassLoader getInstance() {
        String[] args = CommandConfig.getArgs(JvmClassLoaderFactory.class.getName());
        Command command = Command.parse(args);
        Classpath classpath = new Classpath(command);
        JvmClassLoader jvmClassLoader = new JvmClassLoader(classpath);
        return jvmClassLoader;
    }

}
