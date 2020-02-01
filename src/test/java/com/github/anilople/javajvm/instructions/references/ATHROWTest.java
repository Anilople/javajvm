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

    public static void runtime() {
        throw new RuntimeException("sdf");
    }

    public static void main(String[] args) {
        catchOne();
    }

    @Test
    void execute() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.run();

        assertTrue(jvmThreadRunner.isExecuted(ATHROW.class));
    }

    @Test
    void uncaughtException() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(
                JvmThreadFactory.createFromStaticMethod(
                        this.getClass(),
                        "runtime",
                        "()V"
                )
        );

        jvmThreadRunner.run();

        assertTrue(jvmThreadRunner.isExecuted(ATHROW.class));
    }
}