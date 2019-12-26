package com.github.anilople.javajvm.instructions.math.add;

import com.github.anilople.javajvm.heap.JvmClassLoader;
import com.github.anilople.javajvm.heap.JvmMethod;
import com.github.anilople.javajvm.helper.HighOrderFunctions;
import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectArrayReference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectReference;
import com.github.anilople.javajvm.utils.JvmMethodUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

class LADDTest {

    public static void main(String[] args) {
        long a = 0xFFFFFFFFFFFFL;
        long b = 0x1;
        long c = a + b;
    }

    private final Consumer<JvmThread> afterInstructionExecutionListener = jvmThread -> {
        // in the LADDTest's "main" method, so after Instruction "LADD",
        long value = jvmThread.currentFrame().getOperandStacks().popLongValue();
        Assertions.assertEquals(value, 0xFFFFFFFFFFFFL + 0x1);
        // remember that push it back
        jvmThread.currentFrame().getOperandStacks().pushLongValue(value);
    };

    @Test
    void execute() {

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.addAfterInstructionExecutionListener(
                LADD.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), afterInstructionExecutionListener)
        );

        jvmThreadRunner.run();
        
    }
}