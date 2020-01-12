package com.github.anilople.javajvm.instructions.math.shift;

import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.helper.OperandStacksHelper;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

class LSHRTest {

    public static void main(String[] args) {
        long value1 = -1;
        int value2 = 3;
        long after = value1 >> value2;
    }

    @Test
    void execute() {

        final BiConsumer<Instruction, JvmThread> afterListener = (instruction, jvmThread) -> {
            assertEquals(instruction.getClass(), LSHR.class);
            OperandStacksHelper.checkTopLong(
                    jvmThread.currentFrame().getOperandStacks(),
                    aLong -> assertEquals(aLong, -1)
            );
        };

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.addAfterInstructionExecutionListener(LSHR.class, afterListener);

        jvmThreadRunner.run();

        assertTrue(jvmThreadRunner.isExecuted(LSHR.class));
    }
    
}