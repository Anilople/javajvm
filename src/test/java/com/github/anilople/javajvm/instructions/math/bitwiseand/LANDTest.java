package com.github.anilople.javajvm.instructions.math.bitwiseand;

import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.helper.OperandStacksHelper;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Native;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

class LANDTest {

    public static void main(String[] args) {
        // value copy from Long.MIN_VALUE
        long min = 0x8000000000000000L;
        // value copy from Long.MAX_VALUE
        long max = 0x7fffffffffffffffL;

        long value = min & max;
    }

    @Test
    void execute() {
        final BiConsumer<Instruction, JvmThread> afterListener = (instruction, jvmThread) -> {
            assertEquals(instruction.getClass(), LAND.class);
            OperandStacksHelper.checkTopLong(
                    jvmThread.currentFrame().getOperandStacks(),
                    aLong -> assertEquals(aLong, 0)
            );
        };

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));
        jvmThreadRunner.addAfterInstructionExecutionListener(LAND.class, afterListener);

        jvmThreadRunner.run();

        assertTrue(jvmThreadRunner.isExecuted(LAND.class));
    }

}