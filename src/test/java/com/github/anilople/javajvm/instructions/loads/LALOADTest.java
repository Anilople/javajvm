package com.github.anilople.javajvm.instructions.loads;

import com.github.anilople.javajvm.helper.HighOrderFunctions;
import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

class LALOADTest {

    public static void main(String[] args) {
        long[] longs = new long[10];
        longs[1] = 34;
        longs[2] = longs[1];
    }
    
    @Test
    void execute() {
        final BiConsumer<Instruction, JvmThread> beforeListener = (instruction, jvmThread) -> {
            assertTrue(instruction instanceof LALOAD);
            int index = jvmThread.currentFrame().getOperandStacks().popIntValue();
            // check the array index
            assertEquals(1, index);
            jvmThread.currentFrame().getOperandStacks().pushIntValue(index);
        };

        final BiConsumer<Instruction, JvmThread> afterListener = (instruction, jvmThread) -> {
            long fromOperandStack = jvmThread.currentFrame().getOperandStacks().popLongValue();
            assertEquals(34, fromOperandStack);
            jvmThread.currentFrame().getOperandStacks().pushLongValue(fromOperandStack);
        };

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.addBeforeInstructionExecutionListener(
                LALOAD.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), beforeListener)
        );

        jvmThreadRunner.addAfterInstructionExecutionListener(
                LALOAD.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), afterListener)
        );

        jvmThreadRunner.run();
    }
}