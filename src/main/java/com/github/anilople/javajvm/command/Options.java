package com.github.anilople.javajvm.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     */
    private String Xjre;

    /**
     * parse command line
     * @param options will change in this function
     * @param args arguments in command line
     * @param start which location to start parsing
     * @return location of the part which need to parse
     */
    public static int parse(Options options, String[] args, int start) {
        int newStart = start;
        while(newStart < args.length) {
            String nowArg = args[newStart];
            if("-version".equals(nowArg)) {
                options.versionFlag = true;
                newStart += 1;
            } else if("-?".equals(nowArg) || "-help".equals(nowArg)) {
                options.helpFlag = true;
                newStart += 1;
            } else if("-classpath".equals(nowArg) || "-cp".equals(nowArg)) {
                options.classpath = args[newStart + 1];
                newStart += 2;
            } else if("-Xjre".equals(nowArg)) {
                options.Xjre = args[newStart + 1];
                newStart += 2;
            } else if(args[newStart].startsWith("-")){
                logger.warn("{} is not recognized", nowArg);
                newStart += 1;
            } else {
                break;
            }
        }
        return newStart;
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
