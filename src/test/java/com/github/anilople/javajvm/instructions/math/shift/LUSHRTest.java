package com.github.anilople.javajvm.instructions.math.shift;

import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.helper.OperandStacksHelper;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

class LUSHRTest {

    public static void main(String[] args) {
        long value1 = -1;
        int value2 = 1;
        long after = value1 >>> value2;
    }

    @Test
    void execute() {

        final BiConsumer<Instruction, JvmThread> afterListener = (instruction, jvmThread) -> {
            assertEquals(instruction.getClass(), LUSHR.class);
            OperandStacksHelper.checkTopLong(
                    jvmThread.currentFrame().getOperandStacks(),
                    aLong -> assertEquals(aLong, Long.MAX_VALUE)
            );
        };

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.addAfterInstructionExecutionListener(LUSHR.class, afterListener);

        jvmThreadRunner.run();

        assertTrue(jvmThreadRunner.isExecuted(LUSHR.class));
    }

}