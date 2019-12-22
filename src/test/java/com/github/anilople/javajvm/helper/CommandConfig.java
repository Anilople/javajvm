package com.github.anilople.javajvm.helper;

import java.util.Map;
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
                System.getProperty("java.home"),
                // without class name, default is
                className
        };
    }

    public static void main(String[] args) {
        System.out.println("System's environment:");
        Map<String, String> envs = System.getenv();
        for(Map.Entry<String, String> entry : envs.entrySet()) {
            System.out.printf("%s = %s\n", entry.getKey(), entry.getValue());
        }

        System.out.println();
        System.out.println("####################");
        System.out.println();
        System.out.println("System's properties");
        Properties properties = System.getProperties();
        properties.forEach(
                (key, value) -> System.out.println(key + " = " + value)
        );
    }
}
