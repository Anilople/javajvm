package com.github.anilople.javajvm.instructions.stores;

import com.github.anilople.javajvm.helper.HighOrderFunctions;
import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import com.github.anilople.javajvm.runtimedataarea.reference.BaseTypeArrayReference;
import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

class LASTORETest {

    public static void main(String[] args) {
        long[] longs = new long[10];
        longs[1] = 34;
    }

    private BaseTypeArrayReference baseTypeArrayReference = null;

    @Test
    void execute() {

        final BiConsumer<Instruction, JvmThread> beforeListener = (instruction, jvmThread) -> {
            assertTrue(instruction instanceof LASTORE);
            long longValue = jvmThread.currentFrame().getOperandStacks().popLongValue();
            int index = jvmThread.currentFrame().getOperandStacks().popIntValue();
            this.baseTypeArrayReference = (BaseTypeArrayReference) jvmThread.currentFrame().getOperandStacks().popReference();
            // check the array value
            assertEquals(34, longValue);
            // check the array index
            assertEquals(1, index);

            // push them back
            jvmThread.currentFrame().getOperandStacks().pushReference(this.baseTypeArrayReference);
            jvmThread.currentFrame().getOperandStacks().pushIntValue(index);
            jvmThread.currentFrame().getOperandStacks().pushLongValue(longValue);
        };

        final BiConsumer<Instruction, JvmThread> afterListener = (instruction, jvmThread) -> {
            assertNotNull(baseTypeArrayReference);
            assertEquals(34, baseTypeArrayReference.getLongValue(1));
        };

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.addBeforeInstructionExecutionListener(
                LASTORE.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), beforeListener)
        );

        jvmThreadRunner.addAfterInstructionExecutionListener(
                LASTORE.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), afterListener)
        );

        jvmThreadRunner.run();

    }
}