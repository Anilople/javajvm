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

class SASTORETest {

    public static void main(String[] args) {
        short[] shorts = new short[999];
        shorts[666] = 666;
    }


    private BaseTypeArrayReference baseTypeArrayReference = null;

    @Test
    void execute() {

        final BiConsumer<Instruction, JvmThread> beforeListener = (instruction, jvmThread) -> {
            assertTrue(instruction instanceof SASTORE);
            short shortValue = jvmThread.currentFrame().getOperandStacks().popShortValue();
            int index = jvmThread.currentFrame().getOperandStacks().popIntValue();
            this.baseTypeArrayReference = (BaseTypeArrayReference) jvmThread.currentFrame().getOperandStacks().popReference();
            // check the array value
            assertEquals(666, shortValue);
            // check the array index
            assertEquals(666, index);

            // push them back
            jvmThread.currentFrame().getOperandStacks().pushReference(this.baseTypeArrayReference);
            jvmThread.currentFrame().getOperandStacks().pushIntValue(index);
            jvmThread.currentFrame().getOperandStacks().pushShortValue(shortValue);
        };

        final BiConsumer<Instruction, JvmThread> afterListener = (instruction, jvmThread) -> {
            assertNotNull(baseTypeArrayReference);
            assertEquals(666, baseTypeArrayReference.getShortValue(666));
        };

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.addBeforeInstructionExecutionListener(
                SASTORE.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), beforeListener)
        );

        jvmThreadRunner.addAfterInstructionExecutionListener(
                SASTORE.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), afterListener)
        );

        jvmThreadRunner.run();

    }
}