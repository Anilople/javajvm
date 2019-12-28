package com.github.anilople.javajvm.instructions.stores;

import com.github.anilople.javajvm.helper.HighOrderFunctions;
import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.instructions.loads.BALOAD;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import com.github.anilople.javajvm.runtimedataarea.reference.BaseTypeArrayReference;
import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

class BASTORETest {

    public static void main(String[] args) {
        byte[] bytes = new byte[24];
        bytes[23] = (byte) 3;
    }

    private BaseTypeArrayReference baseTypeArrayReference = null;

    @Test
    void execute() {

        final BiConsumer<Instruction, JvmThread> beforeListener = (instruction, jvmThread) -> {
            assertTrue(instruction instanceof BASTORE);
            int value = jvmThread.currentFrame().getOperandStacks().popIntValue();
            int index = jvmThread.currentFrame().getOperandStacks().popIntValue();
            this.baseTypeArrayReference = (BaseTypeArrayReference) jvmThread.currentFrame().getOperandStacks().popReference();
            // check the array value
            assertEquals(3, value);
            // check the array index
            assertEquals(23, index);

            // push them back
            jvmThread.currentFrame().getOperandStacks().pushReference(this.baseTypeArrayReference);
            jvmThread.currentFrame().getOperandStacks().pushIntValue(index);
            jvmThread.currentFrame().getOperandStacks().pushIntValue(value);
        };

        final BiConsumer<Instruction, JvmThread> afterListener = (instruction, jvmThread) -> {
            assertNotNull(baseTypeArrayReference);
            assertEquals(3, baseTypeArrayReference.getByteValue(23));
        };

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.addBeforeInstructionExecutionListener(
                BASTORE.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), beforeListener)
        );

        jvmThreadRunner.addAfterInstructionExecutionListener(
                BASTORE.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), afterListener)
        );

        jvmThreadRunner.run();

    }
}