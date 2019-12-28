package com.github.anilople.javajvm.instructions.loads;

import com.github.anilople.javajvm.helper.HighOrderFunctions;
import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

class BALOADTest {

    public static void main(String[] args) {
        boolean[] booleans = new boolean[20];
        booleans[8] = true;
        booleans[9] = booleans[8];
    }

    @Test
    void execute() {

        final BiConsumer<Instruction, JvmThread> beforeListener = (instruction, jvmThread) -> {
            assertTrue(instruction instanceof BALOAD);
            int index = jvmThread.currentFrame().getOperandStacks().popIntValue();
            // check the array index
            assertEquals(8, index);
            jvmThread.currentFrame().getOperandStacks().pushIntValue(index);
        };

        final BiConsumer<Instruction, JvmThread> afterListener = (instruction, jvmThread) -> {
            boolean fromOperandStack = jvmThread.currentFrame().getOperandStacks().popBooleanValue();
            assertTrue(fromOperandStack);
            jvmThread.currentFrame().getOperandStacks().pushBooleanValue(fromOperandStack);
        };

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.addBeforeInstructionExecutionListener(
                BALOAD.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), beforeListener)
        );

        jvmThreadRunner.addAfterInstructionExecutionListener(
                BALOAD.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), afterListener)
        );

        jvmThreadRunner.run();
    }
}