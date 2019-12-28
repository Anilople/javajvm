package com.github.anilople.javajvm.instructions.math.add;

import com.github.anilople.javajvm.helper.HighOrderFunctions;
import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class FADDTest {

    public static void main(String[] args) {
        float a = 0.3F;
        float b = 3.4F;
        float c = a + b;
    }

    @Test
    void execute() {

        final Consumer<JvmThread> afterInstructionExecutionListener = jvmThread -> {
            // in the FADDTest's "main" method, so after Instruction "FADD",
            float value = jvmThread.currentFrame().getOperandStacks().popFloatValue();
            assertEquals(value, 3.7F);
            // remember that push it back
            jvmThread.currentFrame().getOperandStacks().pushFloatValue(value);
        };

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.addAfterInstructionExecutionListener(
                FADD.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), afterInstructionExecutionListener)
        );

        jvmThreadRunner.run();
    }
}