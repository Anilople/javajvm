package com.github.anilople.javajvm.instructions.math.add;

import com.github.anilople.javajvm.helper.HighOrderFunctions;
import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class DADDTest {

    public static void main(String[] args) {
        double a = 0.2222;
        double b = 0.3333;
        double c = a + b;
    }

    private final Consumer<JvmThread> afterInstructionExecutionListener = jvmThread -> {
        // in the DADDTest's "main" method, so after Instruction "DADD",
        double value = jvmThread.currentFrame().getOperandStacks().popDoubleValue();
        assertEquals(value, 0.5555);
        // remember that push it back
        jvmThread.currentFrame().getOperandStacks().pushDoubleValue(value);
    };
    
    @Test
    void execute() {

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.addAfterInstructionExecutionListener(
                DADD.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), afterInstructionExecutionListener)
        );

        jvmThreadRunner.run();
        
    }
}