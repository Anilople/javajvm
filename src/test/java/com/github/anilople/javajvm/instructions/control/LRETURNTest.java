package com.github.anilople.javajvm.instructions.control;

import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.helper.OperandStacksHelper;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

class LRETURNTest {

    private static long longReturn() {
        return 123456789011L;
    }

    public static void main(String[] args) {
        long value = longReturn();
    }

    @Test
    void execute() {
        final BiConsumer<Instruction, JvmThread> after = (instruction, jvmThread) -> {
            assertEquals(LRETURN.class, instruction.getClass());
            OperandStacksHelper.checkTopLong(
                    jvmThread.currentFrame().getOperandStacks(),
                    aLong -> assertEquals(aLong, 123456789011L)
            );
        };

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.addAfterInstructionExecutionListener(LRETURN.class, after);

        jvmThreadRunner.run();

        assertTrue(jvmThreadRunner.isExecuted(LRETURN.class));
    }
}