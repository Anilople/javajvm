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
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CASTORETest {

    public static void main(String[] args) {
        char[] chars = new char[20];
        int len = chars.length;
        chars[chars.length - 1] = 'k';
    }

    private BaseTypeArrayReference baseTypeArrayReference = null;

    @Test
    void execute() {

        final BiConsumer<Instruction, JvmThread> beforeListener = (instruction, jvmThread) -> {
            assertTrue(instruction instanceof CASTORE);
            char value = jvmThread.currentFrame().getOperandStacks().popCharValue();
            int index = jvmThread.currentFrame().getOperandStacks().popIntValue();
            this.baseTypeArrayReference = (BaseTypeArrayReference) jvmThread.currentFrame().getOperandStacks().popReference();
            // check the array value
            assertEquals('k', value);
            // check the array index
            assertEquals(19, index);

            // push them back
            jvmThread.currentFrame().getOperandStacks().pushReference(this.baseTypeArrayReference);
            jvmThread.currentFrame().getOperandStacks().pushIntValue(index);
            jvmThread.currentFrame().getOperandStacks().pushIntValue(value);
        };

        final BiConsumer<Instruction, JvmThread> afterListener = (instruction, jvmThread) -> {
            assertNotNull(baseTypeArrayReference);
            assertEquals('k', baseTypeArrayReference.getCharValue(19));
        };

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.addBeforeInstructionExecutionListener(
                CASTORE.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), beforeListener)
        );

        jvmThreadRunner.addAfterInstructionExecutionListener(
                CASTORE.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), afterListener)
        );

        jvmThreadRunner.run();
        
    }
}