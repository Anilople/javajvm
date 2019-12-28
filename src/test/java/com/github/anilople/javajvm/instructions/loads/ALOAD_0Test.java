package com.github.anilople.javajvm.instructions.loads;

import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ALOAD_0Test {

    public static void main(String[] args) {
        Object v0 = null;
        Object v1 = v0;
        Object v2 = v1;
        Object v3 = v2;
        Object v4 = v3;

        Object r0 = new Object();
        Object r1 = r0;
        Object r2 = r1;
        Object r3 = r2;
        Object r4 = r3;
    }

    private Reference reference = null;

    @Test
    void execute() {

        final Consumer<JvmThread> beforeALOAD_0ExecuteListener = jvmThread -> {
            Reference reference = jvmThread.currentFrame().getLocalVariables().getReference(0);
            // memory this reference for after listener using
            this.reference = reference;
        };

        final Consumer<JvmThread> afterALOAD_0ExecuteListener = jvmThread -> {
            assertNotNull(reference);
            Reference fromOperandStack = jvmThread.currentFrame().getOperandStacks().popReference();
            // they must be equal
            assertEquals(reference, fromOperandStack);
            // push it back because the stack need balance
            jvmThread.currentFrame().getOperandStacks().pushReference(fromOperandStack);
        };

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.addBeforeInstructionExecutionListener(
                ALOAD_0.class,
                beforeALOAD_0ExecuteListener
        );

        jvmThreadRunner.addAfterInstructionExecutionListener(
                ALOAD_0.class,
                afterALOAD_0ExecuteListener
        );

        jvmThreadRunner.run();

    }
}