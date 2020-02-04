package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ATHROWTest {

    static void catchOne() {
        try {
            tryItOut();
        } catch (Exception e) {
            handleExc(e);
        }
    }

    private static void handleExc(Exception e) {
        System.out.println(e.getMessage());
    }

    private static void tryItOut() throws Exception {
        throw new Exception("try it out!");
    }

    public static void runtimeException() {
        throw new RuntimeException("sdf");
    }

    public static void illegalArgumentException() {
        throw new IllegalArgumentException("arg");
    }

    public static void main(String[] args) {
        throw new RuntimeException("sdf");
    }

    @Test
    void execute() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.run();

        assertTrue(jvmThreadRunner.isExecuted(ATHROW.class));
    }

    @Test
    void uncaughtRuntimeException() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(
                JvmThreadFactory.createFromStaticMethod(
                        this.getClass(),
                        "runtimeException",
                        "()V"
                )
        );

        assertThrows(RuntimeException.class, () -> jvmThreadRunner.run());

        assertTrue(jvmThreadRunner.isExecuted(ATHROW.class));
    }

    @Test
    void uncaughtIllegalArgumentException() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(
                JvmThreadFactory.createFromStaticMethod(
                        this.getClass(),
                        "illegalArgumentException",
                        "()V"
                )
        );

        assertThrows(RuntimeException.class, () -> jvmThreadRunner.run());

        assertTrue(jvmThreadRunner.isExecuted(ATHROW.class));
    }
}