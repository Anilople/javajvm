package com.github.anilople.javajvm.instructions.loads;

import com.github.anilople.javajvm.helper.HighOrderFunctions;
import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

class DALOADTest {

    public static void main(String[] args) {
        double[] doubles = new double[30];
        doubles[6] = 0.333;
        doubles[20] = doubles[6];
    }
    
    @Test
    void execute() {

        final BiConsumer<Instruction, JvmThread> beforeListener = (instruction, jvmThread) -> {
            assertTrue(instruction instanceof DALOAD);
            int index = jvmThread.currentFrame().getOperandStacks().popIntValue();
            // check the array index
            assertEquals(6, index);
            jvmThread.currentFrame().getOperandStacks().pushIntValue(index);
        };

        final BiConsumer<Instruction, JvmThread> afterListener = (instruction, jvmThread) -> {
            double fromOperandStack = jvmThread.currentFrame().getOperandStacks().popDoubleValue();
            assertEquals(0.333, fromOperandStack);
            jvmThread.currentFrame().getOperandStacks().pushDoubleValue(fromOperandStack);
        };

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.addBeforeInstructionExecutionListener(
                DALOAD.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), beforeListener)
        );

        jvmThreadRunner.addAfterInstructionExecutionListener(
                DALOAD.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), afterListener)
        );

        jvmThreadRunner.run();
    }
}