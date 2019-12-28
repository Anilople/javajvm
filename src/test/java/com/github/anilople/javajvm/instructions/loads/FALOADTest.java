package com.github.anilople.javajvm.instructions.loads;

import com.github.anilople.javajvm.helper.HighOrderFunctions;
import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

class FALOADTest {

    public static void main(String[] args) {
        float[] floats = new float[40];
        floats[9] = 0.66F;
        floats[30] = floats[9];
    }
    
    @Test
    void execute() {
        final BiConsumer<Instruction, JvmThread> beforeListener = (instruction, jvmThread) -> {
            assertTrue(instruction instanceof FALOAD);
            int index = jvmThread.currentFrame().getOperandStacks().popIntValue();
            // check the array index
            assertEquals(9, index);
            jvmThread.currentFrame().getOperandStacks().pushIntValue(index);
        };

        final BiConsumer<Instruction, JvmThread> afterListener = (instruction, jvmThread) -> {
            float fromOperandStack = jvmThread.currentFrame().getOperandStacks().popFloatValue();
            assertEquals(0.66F, fromOperandStack);
            jvmThread.currentFrame().getOperandStacks().pushFloatValue(fromOperandStack);
        };

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.addBeforeInstructionExecutionListener(
                FALOAD.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), beforeListener)
        );

        jvmThreadRunner.addAfterInstructionExecutionListener(
                FALOAD.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), afterListener)
        );

        jvmThreadRunner.run();
    }
}