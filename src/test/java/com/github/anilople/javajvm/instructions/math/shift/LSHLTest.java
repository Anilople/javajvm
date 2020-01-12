package com.github.anilople.javajvm.instructions.math.shift;

import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.helper.OperandStacksHelper;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

class LSHLTest {

    public static void main(String[] args) {
        long value1 = 1;
        int value2 = 3;
        long after = value1 << value2;
    }

    @Test
    void execute() {

        final BiConsumer<Instruction, JvmThread> afterListener = (instruction, jvmThread) -> {
            assertEquals(instruction.getClass(), LSHL.class);
            OperandStacksHelper.checkTopLong(
                    jvmThread.currentFrame().getOperandStacks(),
                    aLong -> assertEquals(aLong, 8)
            );
        };

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.addAfterInstructionExecutionListener(LSHL.class, afterListener);

        jvmThreadRunner.run();

        assertTrue(jvmThreadRunner.isExecuted(LSHL.class));
    }

}