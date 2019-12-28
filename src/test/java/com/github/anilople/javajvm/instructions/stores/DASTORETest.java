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

class DASTORETest {
    
    public static void main(String[] args) {
        double[] doubles = new double[30];
        doubles[6] = 0.333;
    }

    private BaseTypeArrayReference baseTypeArrayReference = null;
    
    @Test
    void execute() {
        
        final BiConsumer<Instruction, JvmThread> beforeListener = (instruction, jvmThread) -> {
            assertTrue(instruction instanceof DASTORE);
            double value = jvmThread.currentFrame().getOperandStacks().popDoubleValue();
            int index = jvmThread.currentFrame().getOperandStacks().popIntValue();
            this.baseTypeArrayReference = (BaseTypeArrayReference) jvmThread.currentFrame().getOperandStacks().popReference();
            // check the array value
            assertEquals(0.333, value);
            // check the array index
            assertEquals(6, index);

            // push them back
            jvmThread.currentFrame().getOperandStacks().pushReference(this.baseTypeArrayReference);
            jvmThread.currentFrame().getOperandStacks().pushIntValue(index);
            jvmThread.currentFrame().getOperandStacks().pushDoubleValue(value);
        };

        final BiConsumer<Instruction, JvmThread> afterListener = (instruction, jvmThread) -> {
            assertNotNull(baseTypeArrayReference);
            assertEquals(0.333, baseTypeArrayReference.getDoubleValue(6));
        };

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.addBeforeInstructionExecutionListener(
                DASTORE.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), beforeListener)
        );

        jvmThreadRunner.addAfterInstructionExecutionListener(
                DASTORE.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), afterListener)
        );

        jvmThreadRunner.run();
    }
}