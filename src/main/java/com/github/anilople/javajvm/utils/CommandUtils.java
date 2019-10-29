package com.github.anilople.javajvm.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandUtils {

    private static final Logger logger = LoggerFactory.getLogger(CommandUtils.class);

    /**
     * 1. java [-options] class [args...]
     * 2. java [-options] -jar jarfile [args...]
     * check it is 1 or 2
     * @param args
     * @return
     */
    public static boolean isClassCommand(String[] args) {
        for(String arg : args) {
            if(arg.startsWith("-jar")) {
                return false;
            }
        }
        return true;
    }
}
