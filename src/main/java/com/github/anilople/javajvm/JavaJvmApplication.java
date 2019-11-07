package com.github.anilople.javajvm;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.classfile.MethodInfo;
import com.github.anilople.javajvm.classfile.attributes.AttributeInfo;
import com.github.anilople.javajvm.classfile.attributes.CodeAttribute;
import com.github.anilople.javajvm.classpath.ClassContext;
import com.github.anilople.javajvm.classpath.Classpath;
import com.github.anilople.javajvm.command.Command;
import com.github.anilople.javajvm.constants.JVMConstants;
import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            logger.info("java version {}", JVMConstants.VERSION);
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

    public static void interpret(MethodInfo methodInfo) {
        logger.debug("interpret method {}", methodInfo);
        AttributeInfo[] attributes = methodInfo.getAttributes();
        for (AttributeInfo attributeInfo : attributes) {
            if (CodeAttribute.class.equals(attributeInfo.getClass())) {
                CodeAttribute codeAttribute = CodeAttribute.class.cast(attributeInfo);
                short maxLocals = codeAttribute.getMaxLocals();
                short maxStack = codeAttribute.getMaxStack();
                byte[] bytecode = codeAttribute.getCode();

                JvmThread jvmThread = new JvmThread();
                jvmThread.pushFrame(new Frame(methodInfo.getClassFile(), jvmThread, maxLocals, maxStack));

                loop(jvmThread, bytecode);
            }
        }
    }

    public static void loop(JvmThread jvmThread, byte[] bytecode) {

        while (true) {
            int pc = jvmThread.getPc();

            BytecodeReader bytecodeReader = new BytecodeReader(Arrays.copyOfRange(bytecode, pc, bytecode.length));

            Instruction instruction = Instruction.readInstruction(bytecodeReader);
            logger.debug("read instruction: {}", instruction);
            instruction.fetchOperands(bytecodeReader);
            int nextPc = instruction.execute(jvmThread.currentFrame());

            jvmThread.setPc(nextPc);

        }
    }

    /**
     * run jvm
     */
    public void start() {
        String className = command.getClassName().replace('.', '/');
        logger.debug("class name = {}", className);
        byte[] data = classContext.readClass(className);
        logger.debug("{}'s data bytes: {}", className, data);
        ClassFile classFile = ClassFile.parse(new ClassFile.ClassReader(data));
        logger.debug("{}'s struct: {}", className, classFile);
        MethodInfo[] methods = classFile.getMethods();
        for (MethodInfo methodInfo : methods) {
            logger.debug("method name: {}, {}", methodInfo.getName(), methodInfo);
            if ("main".equals(methodInfo.getName())) {
                interpret(methodInfo);
            }
        }
    }
}
