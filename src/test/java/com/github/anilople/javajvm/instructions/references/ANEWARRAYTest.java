package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.helper.HighOrderFunctions;
import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectArrayReference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

class ANEWARRAYTest {

    public static void main(String[] args) {
        ANEWARRAYTest[] anewarrayTests = new ANEWARRAYTest[6];
    }

    private final Consumer<JvmThread> afterInstructionExecutionListener = jvmThread -> {
        // in the ANEWARRAYTest's "main" method, so after Instruction "ANEWARRAY",
        ObjectArrayReference objectArrayReference = (ObjectArrayReference) jvmThread.currentFrame().getOperandStacks().popReference();
        // the top Object on the operand stack must be the array of "ANEWARRAYTest"
        ObjectReference typeReference = (ObjectReference) objectArrayReference.getTypeReference();
        Assertions.assertTrue(typeReference.getJvmClass().isSameName(ANEWARRAYTest.class));
        // remember that push it back
        jvmThread.currentFrame().getOperandStacks().pushReference(objectArrayReference);
    };

    @Test
    void execute() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));
        
        jvmThreadRunner.addAfterInstructionExecutionListener(
                ANEWARRAY.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), afterInstructionExecutionListener)
        );

        jvmThreadRunner.run();
    }
}