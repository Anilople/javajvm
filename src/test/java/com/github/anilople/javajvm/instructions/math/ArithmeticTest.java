package com.github.anilople.javajvm.instructions.math;

import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.instructions.math.add.DADD;
import com.github.anilople.javajvm.instructions.math.add.FADD;
import com.github.anilople.javajvm.instructions.math.add.IADD;
import com.github.anilople.javajvm.instructions.math.add.LADD;
import com.github.anilople.javajvm.instructions.math.divide.DDIV;
import com.github.anilople.javajvm.instructions.math.divide.FDIV;
import com.github.anilople.javajvm.instructions.math.divide.IDIV;
import com.github.anilople.javajvm.instructions.math.divide.LDIV;
import com.github.anilople.javajvm.instructions.math.multiply.DMUL;
import com.github.anilople.javajvm.instructions.math.multiply.FMUL;
import com.github.anilople.javajvm.instructions.math.multiply.IMUL;
import com.github.anilople.javajvm.instructions.math.multiply.LMUL;
import com.github.anilople.javajvm.instructions.math.subtract.DSUB;
import com.github.anilople.javajvm.instructions.math.subtract.FSUB;
import com.github.anilople.javajvm.instructions.math.subtract.ISUB;
import com.github.anilople.javajvm.instructions.math.subtract.LSUB;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * integration testing of arithmetic
 */
public class ArithmeticTest {

    // int
    public static void intArithmetic() {
        int a = 100;
        int b = a / 3;
        int c = b - 6;
        int d = c * 3;
        int e = d + 4;
    }

    @Test
    public void intTest() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(
                JvmThreadFactory.createFromStaticMethod(
                        this.getClass(),
                        "intArithmetic",
                        "()V"
                )
        );

        Consumer<JvmThread> afterAddListener = jvmThread -> {
            int result = jvmThread.currentFrame().getOperandStacks().popIntValue();

            assertEquals((100 / 3 - 6) * 3 + 4, result);

            jvmThread.currentFrame().getOperandStacks().pushIntValue(result);
        };

        jvmThreadRunner.addAfterInstructionExecutionListener(
                IADD.class,
                afterAddListener
        );

        jvmThreadRunner.run();

        assertTrue(jvmThreadRunner.isExecuted(IADD.class));
        assertTrue(jvmThreadRunner.isExecuted(ISUB.class));
        assertTrue(jvmThreadRunner.isExecuted(IMUL.class));
        assertTrue(jvmThreadRunner.isExecuted(IDIV.class));
    }

    // float
    public static void floatArithmetic() {
        float a = 100F;
        float b = a / 3;
        float c = b - 6;
        float d = c * 3;
        float e = d + 4;
    }

    @Test
    public void floatTest() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(
                JvmThreadFactory.createFromStaticMethod(
                        this.getClass(),
                        "floatArithmetic",
                        "()V"
                )
        );

        Consumer<JvmThread> afterAddListener = jvmThread -> {
            float result = jvmThread.currentFrame().getOperandStacks().popFloatValue();

            assertEquals((100F / 3 - 6) * 3 + 4, result);

            jvmThread.currentFrame().getOperandStacks().pushFloatValue(result);
        };

        jvmThreadRunner.addAfterInstructionExecutionListener(
                FADD.class,
                afterAddListener
        );

        jvmThreadRunner.run();

        assertTrue(jvmThreadRunner.isExecuted(FADD.class));
        assertTrue(jvmThreadRunner.isExecuted(FSUB.class));
        assertTrue(jvmThreadRunner.isExecuted(FMUL.class));
        assertTrue(jvmThreadRunner.isExecuted(FDIV.class));
    }

    // long
    public static void longArithmetic() {
        long a = 100L;
        long b = a / 3;
        long c = b - 6;
        long d = c * 3;
        long e = d + 4;
    }

    @Test
    public void longTest() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(
                JvmThreadFactory.createFromStaticMethod(
                        this.getClass(),
                        "longArithmetic",
                        "()V"
                )
        );

        Consumer<JvmThread> afterAddListener = jvmThread -> {
            long result = jvmThread.currentFrame().getOperandStacks().popLongValue();

            assertEquals((100L / 3 - 6) * 3 + 4, result);

            jvmThread.currentFrame().getOperandStacks().pushLongValue(result);
        };

        jvmThreadRunner.addAfterInstructionExecutionListener(
                LADD.class,
                afterAddListener
        );

        jvmThreadRunner.run();

        assertTrue(jvmThreadRunner.isExecuted(LADD.class));
        assertTrue(jvmThreadRunner.isExecuted(LSUB.class));
        assertTrue(jvmThreadRunner.isExecuted(LMUL.class));
        assertTrue(jvmThreadRunner.isExecuted(LDIV.class));
    }

    // double
    public static void doubleArithmetic() {
        double a = 100D;
        double b = a / 3;
        double c = b - 6;
        double d = c * 3;
        double e = d + 4;
    }

    @Test
    public void doubleTest() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(
                JvmThreadFactory.createFromStaticMethod(
                        this.getClass(),
                        "doubleArithmetic",
                        "()V"
                )
        );

        Consumer<JvmThread> afterAddListener = jvmThread -> {
            double result = jvmThread.currentFrame().getOperandStacks().popDoubleValue();

            assertEquals((100D / 3 - 6) * 3 + 4, result);

            jvmThread.currentFrame().getOperandStacks().pushDoubleValue(result);
        };

        jvmThreadRunner.addAfterInstructionExecutionListener(
                DADD.class,
                afterAddListener
        );

        jvmThreadRunner.run();

        assertTrue(jvmThreadRunner.isExecuted(DADD.class));
        assertTrue(jvmThreadRunner.isExecuted(DSUB.class));
        assertTrue(jvmThreadRunner.isExecuted(DMUL.class));
        assertTrue(jvmThreadRunner.isExecuted(DDIV.class));
    }

}
