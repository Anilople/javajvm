package com.github.anilople.javajvm.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

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

    /**
     * split args to 3 parts: options, class or jarfile, args
     */
    public static List<List<String>> splitToThreePart(String[] args) {
        List<String> optionArgsPart = new ArrayList<String>();
        List<String> namePart = new ArrayList<String>();
        List<String> argsPart = new ArrayList<String>();

        int index = 0;
        // get [-options] part
        while(index < args.length && args[index].startsWith("-")) {
            optionArgsPart.add(args[index]);
            index += 1;
        }
        // get class or -jar jarfile part
        if(index < args.length) {
            if(args[index].startsWith("-jar")) {
                index += 1;
                // jarfile
                // exist an array out of index bug here!!
                namePart.add(args[index]);
            } else {
                // class
                namePart.add(args[index]);
            }
        } else {
            logger.warn("there are no class or jarfile in args : {}", args);
        }

        // get args part
        while(index < args.length) {
            argsPart.add(args[index]);
            index += 1;
        }

        return Arrays.asList(optionArgsPart, namePart, argsPart);
    }
}
