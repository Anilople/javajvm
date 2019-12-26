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

class ASTORE_0Test {

    public static void main(String[] args) {
        ASTORE_0Test v0 = new ASTORE_0Test();
        ASTORE_0Test v1 = v0;
        ASTORE_0Test v2 = v1;
        ASTORE_0Test v3 = v2;
        ASTORE_0Test v4 = v3;
    }

    private ObjectReference objectReference = null;

    private final Consumer<JvmThread> beforeASTORE_0ExecuteListener = jvmThread -> {
        ObjectReference fromOperandStack = (ObjectReference) jvmThread.currentFrame().getOperandStacks().popReference();
        objectReference = fromOperandStack;
        jvmThread.currentFrame().getOperandStacks().pushReference(fromOperandStack);
    };

    private final Consumer<JvmThread> afterALOAD_1ExecuteListener = jvmThread -> {
        assertNotNull(objectReference);
        Reference reference = jvmThread.currentFrame().getLocalVariables().getReference(0);
        assertFalse(reference instanceof NullReference);
        // they must be equal
        assertEquals(objectReference, reference);
    };

    @Test
    void execute() {

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.addBeforeInstructionExecutionListener(
                ASTORE_0.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), beforeASTORE_0ExecuteListener)
        );

        jvmThreadRunner.addAfterInstructionExecutionListener(
                ASTORE_0.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), afterALOAD_1ExecuteListener)
        );

        jvmThreadRunner.run();

    }
}