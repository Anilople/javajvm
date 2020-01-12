package com.github.anilople.javajvm.instructions.math.shift;

import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.helper.OperandStacksHelper;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

class ISHRTest {

    public static void main(String[] args) {
        int value1 = -1;
        int value2 = 3;
        int after = value1 >> value2;
    }

    @Test
    void execute() {

        final BiConsumer<Instruction, JvmThread> afterListener = (instruction, jvmThread) -> {
            assertEquals(instruction.getClass(), ISHR.class);
            OperandStacksHelper.checkTopInt(
                    jvmThread.currentFrame().getOperandStacks(),
                    aInt -> assertEquals(aInt, -1)
            );
        };

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.addAfterInstructionExecutionListener(ISHR.class, afterListener);

        jvmThreadRunner.run();

        assertTrue(jvmThreadRunner.isExecuted(ISHR.class));
    }
    
}