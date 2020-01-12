package com.github.anilople.javajvm.instructions.control;

import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.helper.OperandStacksHelper;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

class FRETURNTest {

    private static float floatReturn() {
        return 0.777F;
    }

    public static void main(String[] args) {
        float value = floatReturn();
    }

    @Test
    void execute() {
        final BiConsumer<Instruction, JvmThread> after = (instruction, jvmThread) -> {
            assertEquals(FRETURN.class, instruction.getClass());
            OperandStacksHelper.checkTopFloat(
                    jvmThread.currentFrame().getOperandStacks(),
                    aFloat -> assertEquals(aFloat, 0.777F)
            );
        };

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.addAfterInstructionExecutionListener(FRETURN.class, after);

        jvmThreadRunner.run();

        assertTrue(jvmThreadRunner.isExecuted(FRETURN.class));
    }
}