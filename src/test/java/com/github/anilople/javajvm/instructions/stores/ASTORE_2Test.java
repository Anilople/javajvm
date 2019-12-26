package com.github.anilople.javajvm.instructions.stores;

import com.github.anilople.javajvm.helper.HighOrderFunctions;
import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.NullReference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectReference;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class ASTORE_2Test {

    public static void main(String[] args) {
        ASTORE_2Test v0 = new ASTORE_2Test();
        ASTORE_2Test v1 = v0;
        ASTORE_2Test v2 = v1;
        ASTORE_2Test v3 = v2;
        ASTORE_2Test v4 = v3;
    }

    private ObjectReference objectReference = null;

    private final Consumer<JvmThread> beforeASTORE_2ExecuteListener = jvmThread -> {
        ObjectReference fromOperandStack = (ObjectReference) jvmThread.currentFrame().getOperandStacks().popReference();
        objectReference = fromOperandStack;
        jvmThread.currentFrame().getOperandStacks().pushReference(fromOperandStack);
    };

    private final Consumer<JvmThread> afterALOAD_1ExecuteListener = jvmThread -> {
        assertNotNull(objectReference);
        Reference reference = jvmThread.currentFrame().getLocalVariables().getReference(2);
        assertFalse(reference instanceof NullReference);
        // they must be equal
        assertEquals(objectReference, reference);
    };

    @Test
    void execute() {

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.addBeforeInstructionExecutionListener(
                ASTORE_2.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), beforeASTORE_2ExecuteListener)
        );

        jvmThreadRunner.addAfterInstructionExecutionListener(
                ASTORE_2.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), afterALOAD_1ExecuteListener)
        );

        jvmThreadRunner.run();

    }
}