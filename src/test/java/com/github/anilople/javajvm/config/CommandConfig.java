package com.github.anilople.javajvm.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

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
                "C:\\Program Files\\Java\\jre1.8.0_231",
                // without class name, default is
                className
        };
    }

}
