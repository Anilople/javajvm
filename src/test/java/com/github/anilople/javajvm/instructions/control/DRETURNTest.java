package com.github.anilople.javajvm.instructions.control;

import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.helper.OperandStacksHelper;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

class DRETURNTest {

    private static double doubleReturn() {
        return 1.66D;
    }

    public static void main(String[] args) {
        double value = doubleReturn();
    }

    @Test
    void execute() {
        final BiConsumer<Instruction, JvmThread> after = (instruction, jvmThread) -> {
            assertEquals(DRETURN.class, instruction.getClass());
            OperandStacksHelper.checkTopDouble(
                    jvmThread.currentFrame().getOperandStacks(),
                    aDouble -> assertEquals(aDouble, 1.66D)
            );
        };

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.addAfterInstructionExecutionListener(DRETURN.class, after);

        jvmThreadRunner.run();

        assertTrue(jvmThreadRunner.isExecuted(DRETURN.class));
    }
}