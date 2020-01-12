package com.github.anilople.javajvm.instructions.conversions;

import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.helper.OperandStacksHelper;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;

class D2FTest {

    public static void main(String[] args) {
        double doubleValue = 6.1234567890D;
        float floatValue = (float) doubleValue;
    }

    @Test
    void execute() {

        final BiConsumer<Instruction, JvmThread> beforeConversion = (instruction, jvmThread) -> {
            assertEquals(D2F.class, instruction.getClass());
            OperandStacksHelper.checkTopDouble(
                    jvmThread.currentFrame().getOperandStacks(),
                    aDouble -> assertEquals(6.1234567890D, aDouble)
            );
        };

        final BiConsumer<Instruction, JvmThread> afterConversion = (instruction, jvmThread) -> {
            assertEquals(D2F.class, instruction.getClass());
            OperandStacksHelper.checkTopFloat(
                    jvmThread.currentFrame().getOperandStacks(),
                    aFloat -> assertEquals((float)6.1234567890D, aFloat)
            );
        };

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.addBeforeInstructionExecutionListener(D2F.class, beforeConversion);
        jvmThreadRunner.addAfterInstructionExecutionListener(D2F.class, afterConversion);

        jvmThreadRunner.run();

        assertTrue(jvmThreadRunner.isExecuted(D2F.class));
    }
}