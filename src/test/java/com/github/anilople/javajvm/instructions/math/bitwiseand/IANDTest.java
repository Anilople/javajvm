package com.github.anilople.javajvm.instructions.math.bitwiseand;

import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.helper.OperandStacksHelper;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

class IANDTest {

    public static void main(String[] args) {
        int a = 0xF000_FFFF;
        int b = 0xFFFF_000F;
        int c = a & b;
    }

    @Test
    void execute() {
        final BiConsumer<Instruction, JvmThread> afterListener = (instruction, jvmThread) -> {
            assertEquals(instruction.getClass(), IAND.class);
            OperandStacksHelper.checkTopInt(
                    jvmThread.currentFrame().getOperandStacks(),
                    aInt -> assertEquals(aInt, 0xF000_000F)
            );
        };

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));
        jvmThreadRunner.addAfterInstructionExecutionListener(IAND.class, afterListener);

        jvmThreadRunner.run();

        assertTrue(jvmThreadRunner.isExecuted(IAND.class));
    }
}