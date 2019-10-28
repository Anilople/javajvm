package com.github.anilople.javajvm;

import com.github.anilople.javajvm.command.Command;
import com.github.anilople.javajvm.constants.JVMConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaJvmApplication {

    private static final Logger logger = LoggerFactory.getLogger(JavaJvmApplication.class);

    private Command command;

    /**
     * in java, there 4 way to use java command
     * 1. java [-options] class [args...]
     * 2. java [-options] -jar jarfile [args...]
     * 3. javaw [-options] class [args...]
     * 4. javaw [-options] -jar jarfile [args...]
     *
     * we are using 1 or 2, not using 3 and 4
     * @param args [args...] above
     */
    public static void main(String[] args) {
        Command command = Command.parse(args);
        logger.info("command : {}", command);
        if(command.getOptions().isVersionFlag()) {
            logger.info("java version {}", JVMConstants.VERSION);
        } else if(command.getOptions().isHelpFlag()) {
            printUsage();
        } else if(null != command.getClassName()) {
            JavaJvmApplication javaJvmApplication = new JavaJvmApplication(command);
            javaJvmApplication.start();
        }
    }

    public static void printUsage() {

    }

    public JavaJvmApplication(Command command) {
        this.command = command;
    }

    public void start() {

    }
}
