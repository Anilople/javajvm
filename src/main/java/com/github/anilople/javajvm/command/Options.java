package com.github.anilople.javajvm.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * [-options]
 */
public class Options {

    private static final Logger logger = LoggerFactory.getLogger(Options.class);

    /**
     * -version
     */
    private boolean versionFlag;

    /**
     * -?
     * or
     * -help
     */
    private boolean helpFlag;

    /**
     * value after
     * -classpath
     * or
     * -cp
     */
    private String classpath;

    /**
     * value after
     * -Xjre
     *
     * if no value in user's input,
     * the default is get by method {@link System#getProperty(String)}
     * {@code System.getProperty("java.home")}
     */
    private String Xjre;

    /**
     * parse command line
     *
     * @param options will change in this function
     * @param args    arguments in command line
     * @param start   which location to start parsing
     * @return location of the part which need to parse
     */
    public static int parse(Options options, String[] args, int start) {
        int newStart = start;
        while (newStart < args.length) {
            String nowArg = args[newStart];
            if ("-version".equals(nowArg)) {
                options.versionFlag = true;
                newStart += 1;
            } else if ("-?".equals(nowArg) || "-help".equals(nowArg)) {
                options.helpFlag = true;
                newStart += 1;
            } else if ("-classpath".equals(nowArg) || "-cp".equals(nowArg)) {
                options.classpath = args[newStart + 1];
                newStart += 2;
            } else if ("-Xjre".equals(nowArg)) {
                options.Xjre = args[newStart + 1];
                newStart += 2;
            } else if (args[newStart].startsWith("-")) {
                logger.warn("{} is not recognized", nowArg);
                newStart += 1;
            } else {
                break;
            }
        }

        // parse finished,
        // set the default values
        if(null == options.Xjre) {
            options.Xjre = System.getProperty("java.home");
        }

        return newStart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Options options = (Options) o;
        return isVersionFlag() == options.isVersionFlag() &&
                isHelpFlag() == options.isHelpFlag() &&
                getClasspath().equals(options.getClasspath()) &&
                getXjre().equals(options.getXjre());
    }

    @Override
    public int hashCode() {
        return Objects.hash(isVersionFlag(), isHelpFlag(), getClasspath(), getXjre());
    }

    public boolean isVersionFlag() {
        return versionFlag;
    }

    public boolean isHelpFlag() {
        return helpFlag;
    }

    public String getClasspath() {
        return classpath;
    }

    public String getXjre() {
        return Xjre;
    }
}
