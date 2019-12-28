package com.github.anilople.javajvm.instructions.stores;

import com.github.anilople.javajvm.helper.HighOrderFunctions;
import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ASTORE_2Test {

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

        final Consumer<JvmThread> beforeASTORE_2ExecuteListener = jvmThread -> {
            Reference fromOperandStack = jvmThread.currentFrame().getOperandStacks().popReference();
            this.reference = fromOperandStack;
            jvmThread.currentFrame().getOperandStacks().pushReference(fromOperandStack);
        };

        final Consumer<JvmThread> afterASTORE_2ExecuteListener = jvmThread -> {
            assertNotNull(reference);
            Reference reference = jvmThread.currentFrame().getLocalVariables().getReference(2);
            // they must be equal
            assertEquals(this.reference, reference);
        };

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.addBeforeInstructionExecutionListener(
                ASTORE_2.class,
                beforeASTORE_2ExecuteListener
        );

        jvmThreadRunner.addAfterInstructionExecutionListener(
                ASTORE_2.class,
                afterASTORE_2ExecuteListener
        );

        jvmThreadRunner.run();
    }
}