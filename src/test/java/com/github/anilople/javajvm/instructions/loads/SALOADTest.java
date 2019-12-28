package com.github.anilople.javajvm.instructions.loads;

import com.github.anilople.javajvm.helper.HighOrderFunctions;
import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

class SALOADTest {

    public static void main(String[] args) {
        short[] shorts = new short[999];
        shorts[666] = 666;
        shorts[777] = shorts[666];
    }
    
    @Test
    void execute() {
        final BiConsumer<Instruction, JvmThread> beforeListener = (instruction, jvmThread) -> {
            assertTrue(instruction instanceof SALOAD);
            int index = jvmThread.currentFrame().getOperandStacks().popIntValue();
            // check the array index
            assertEquals(666, index);
            jvmThread.currentFrame().getOperandStacks().pushIntValue(index);
        };

        final BiConsumer<Instruction, JvmThread> afterListener = (instruction, jvmThread) -> {
            short fromOperandStack = jvmThread.currentFrame().getOperandStacks().popShortValue();
            assertEquals(666, fromOperandStack);
            jvmThread.currentFrame().getOperandStacks().pushShortValue(fromOperandStack);
        };

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.addBeforeInstructionExecutionListener(
                SALOAD.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), beforeListener)
        );

        jvmThreadRunner.addAfterInstructionExecutionListener(
                SALOAD.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), afterListener)
        );

        jvmThreadRunner.run();
    }
}