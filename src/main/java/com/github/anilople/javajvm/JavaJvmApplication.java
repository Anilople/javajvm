package com.github.anilople.javajvm;

import com.github.anilople.javajvm.classpath.ClassContext;
import com.github.anilople.javajvm.classpath.Classpath;
import com.github.anilople.javajvm.command.Command;
import com.github.anilople.javajvm.constants.JvmProperties;
import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.heap.JvmClassLoader;
import com.github.anilople.javajvm.heap.JvmMethod;
import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;

public class JavaJvmApplication {

    private static final Logger logger = LoggerFactory.getLogger(JavaJvmApplication.class);

    private Command command;

    private ClassContext classContext;

    /**
     * initial a jvm
     *
     * @param command
     */
    public JavaJvmApplication(Command command) {
        this.command = command;
        this.classContext = new Classpath(command);
    }

    /**
     * in java, there 4 way to use java command
     * 1. java [-options] class [args...]
     * 2. java [-options] -jar jarfile [args...]
     * 3. javaw [-options] class [args...]
     * 4. javaw [-options] -jar jarfile [args...]
     * <p>
     * we are using 1 or 2, not using 3 and 4
     *
     * @param args [args...] above
     */
    public static void main(String[] args) {
        Command command = Command.parse(args);
        logger.info("command : {}", command);
        if (command.getOptions().isVersionFlag()) {
            logger.info("java version {}", JvmProperties.VERSION);
        } else if (command.getOptions().isHelpFlag()) {
            printUsage();
        } else if (null != command.getClassName()) {
            JavaJvmApplication javaJvmApplication = new JavaJvmApplication(command);
            javaJvmApplication.start();
        }
    }

    /**
     * print usage to user
     */
    public static void printUsage() {

    }

    public static void interpret(JvmMethod jvmMethod) {
        logger.debug("interpret method {}", jvmMethod);
        JvmThread jvmThread = new JvmThread();
        jvmThread.pushFrame(new Frame(jvmThread, jvmMethod));
        loop(jvmThread);
    }

    public static void loop(JvmThread jvmThread) {
        logger.trace("start loop: {}", jvmThread);

        while (jvmThread.existFrame()) {
            jvmThread.currentFrame().traceStatus();

            // frame -> method -> method's code
            byte[] bytecode = jvmThread.currentFrame().getJvmMethod().getCode();

            int pc = jvmThread.currentFrame().getNextPc();
            BytecodeReader bytecodeReader = new BytecodeReader(Arrays.copyOfRange(bytecode, pc, bytecode.length));

            Instruction instruction = Instruction.readInstruction(bytecodeReader);
            logger.debug("read instruction: {}", instruction);
            instruction.fetchOperands(bytecodeReader);

            // change nextPc in top frame
            instruction.execute(jvmThread.currentFrame());

//            jvmThread.setPc(nextPc);

        }

        logger.trace("loop finished: {}", jvmThread);
    }

    /**
     * run jvm
     */
    public void start() {
        String className = command.getClassName().replace('.', File.separatorChar);
        logger.debug("class name = {}", className);

        JvmClassLoader jvmClassLoader = new JvmClassLoader(this.classContext);
        JvmClass jvmClass = jvmClassLoader.loadClass(className);

        for(JvmMethod jvmMethod : jvmClass.getJvmMethods()) {
            logger.debug("jvm method : {}", jvmMethod);
            if("main".equals(jvmMethod.getName())) {
                interpret(jvmMethod);
            }
        }
    }
}
