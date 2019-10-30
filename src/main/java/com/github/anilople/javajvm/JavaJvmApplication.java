package com.github.anilople.javajvm;

import com.github.anilople.javajvm.classpath.ClassContext;
import com.github.anilople.javajvm.classpath.Classpath;
import com.github.anilople.javajvm.command.Command;
import com.github.anilople.javajvm.constants.JVMConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class JavaJvmApplication {

    private static final Logger logger = LoggerFactory.getLogger(JavaJvmApplication.class);

    private Command command;

    private ClassContext classContext;

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

    /**
     * print usage to user
     */
    public static void printUsage() {

    }

    /**
     * initial a jvm
     * @param command
     */
    public JavaJvmApplication(Command command) {
        this.command = command;
        this.classContext = new Classpath(command);
    }

    /**
     * run jvm
     */
    public void start() {
        String className = command.getClassName().replace('.', '/');
        logger.info("class name = {}", className);
        byte[] data = classContext.readClass(className);
        logger.info("{}'s data bytes: {}", className, data);
    }
}
