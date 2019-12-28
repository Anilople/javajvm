package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.helper.HighOrderFunctions;
import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.ArrayReference;
import com.github.anilople.javajvm.runtimedataarea.reference.NullReference;
import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class NEWARRAYTest {

    public static void main(String[] args) {
        // byte
        byte[] bytes0 = new byte[0];
        byte[] bytes = new byte[1];
        // short
        short[] shorts0 = new short[0];
        short[] shorts =  new short[2];
        // char
        char[] chars0 = new char[0];
        char[] chars = new char[4];
        // int
        int[] ints0 = new int[0];
        int[] ints = new int[8];
        // long
        long[] longs0 = new long[0];
        long[] longs = new long[16];
    }

    @Test
    void execute() {

        final Consumer<JvmThread> afterInstructionExecutionListener = jvmThread -> {
            Reference reference = jvmThread.currentFrame().getOperandStacks().popReference();
            // check not null
            assertFalse(reference instanceof NullReference);
            assertTrue(reference instanceof ArrayReference);
            jvmThread.currentFrame().getOperandStacks().pushReference(reference);
        };

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.addAfterInstructionExecutionListener(
                NEWARRAY.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), afterInstructionExecutionListener)
        );

        jvmThreadRunner.run();

    }
}