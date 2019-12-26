package com.github.anilople.javajvm.instructions.math.add;

import com.github.anilople.javajvm.heap.JvmClassLoader;
import com.github.anilople.javajvm.heap.JvmMethod;
import com.github.anilople.javajvm.helper.HighOrderFunctions;
import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import com.github.anilople.javajvm.utils.JvmMethodUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

class IADDTest {

    public static void main(String[] args) {
        int i = 3;
        int j = 5;
        int c = i + j;
    }

    private final Consumer<JvmThread> afterInstructionExecutionListener = jvmThread -> {
        // there must a int number on the top of operand stack now
        int number = jvmThread.currentFrame().getOperandStacks().popIntValue();
        Assertions.assertEquals(8, number);
        // remember that push it back
        jvmThread.currentFrame().getOperandStacks().pushIntValue(number);
    };

    @Test
    void execute() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.addAfterInstructionExecutionListener(
                IADD.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), afterInstructionExecutionListener)
        );

        jvmThreadRunner.run();
    }

}