package com.github.anilople.javajvm.config;

/**
 * Every time when we need run a jvm,
 * there are some options, class name or jar file name we need to input.
 * For making more easy to test code,
 * write this class to read config from config file
 */
public class CommandConfig {

    private CommandConfig() {

    }

    /**
     * command when run a jvm
     * @param className without suffix ".class"
     * @return
     */
    public static String[] getArgs(String className) {
        return new String[]{
                "-Xjre",
                System.getProperty("java.home"),
                // without class name, default is
                className
        };
    }

}
