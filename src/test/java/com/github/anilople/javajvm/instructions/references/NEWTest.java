package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.helper.HighOrderFunctions;
import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectReference;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class NEWTest {

    public static void main(String[] args) {
        NEWTest newTest = new NEWTest();
    }

    private final Consumer<JvmThread> afterInstructionExecutionListener = jvmThread -> {
        // in the NEWTest's "main" method, so after Instruction "NEW",
        ObjectReference objectReference = (ObjectReference) jvmThread.currentFrame().getOperandStacks().popReference();
        // the top Object on the operand stack must be class "NEWTest"
        assertTrue(objectReference.getJvmClass().isSameName(NEWTest.class));
        // remember that push it back
        jvmThread.currentFrame().getOperandStacks().pushReference(objectReference);
    };

    @Test
    void execute() {

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(NEWTest.class));

        jvmThreadRunner.addAfterInstructionExecutionListener(
                NEW.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), afterInstructionExecutionListener)
        );

        jvmThreadRunner.run();
    }
}