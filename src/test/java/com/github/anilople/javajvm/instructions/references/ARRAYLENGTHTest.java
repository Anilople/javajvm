package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.helper.HighOrderFunctions;
import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.ArrayReference;
import com.github.anilople.javajvm.runtimedataarea.reference.NullReference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectArrayReference;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class ARRAYLENGTHTest {

    public static void main(String[] args) {
        ARRAYLENGTHTest[] arraylengthTests = new ARRAYLENGTHTest[20];
        int length = arraylengthTests.length;
    }

    @Test
    void execute() {

        final Consumer<JvmThread> afterInstructionExecutionListener = jvmThread -> {
            int length = jvmThread.currentFrame().getOperandStacks().popIntValue();
            assertEquals(20, length);
            jvmThread.currentFrame().getOperandStacks().pushIntValue(length);
        };

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.addAfterInstructionExecutionListener(
                ARRAYLENGTH.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), afterInstructionExecutionListener)
        );

        jvmThreadRunner.run();
    }
}