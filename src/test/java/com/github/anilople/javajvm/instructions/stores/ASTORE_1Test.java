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

class ASTORE_1Test {

    public static void main(String[] args) {
        ASTORE_1Test v0 = new ASTORE_1Test();
        ASTORE_1Test v1 = v0;
        ASTORE_1Test v2 = v1;
        ASTORE_1Test v3 = v2;
        ASTORE_1Test v4 = v3;
    }

    private ObjectReference objectReference = null;

    private final Consumer<JvmThread> beforeASTORE_1ExecuteListener = jvmThread -> {
        ObjectReference fromOperandStack = (ObjectReference) jvmThread.currentFrame().getOperandStacks().popReference();
        objectReference = fromOperandStack;
        jvmThread.currentFrame().getOperandStacks().pushReference(fromOperandStack);
    };

    private final Consumer<JvmThread> afterALOAD_1ExecuteListener = jvmThread -> {
        assertNotNull(objectReference);
        Reference reference = jvmThread.currentFrame().getLocalVariables().getReference(1);
        assertFalse(reference instanceof NullReference);
        // they must be equal
        assertEquals(objectReference, reference);
    };

    @Test
    void execute() {

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.addBeforeInstructionExecutionListener(
                ASTORE_1.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), beforeASTORE_1ExecuteListener)
        );

        jvmThreadRunner.addAfterInstructionExecutionListener(
                ASTORE_1.class,
                HighOrderFunctions.toInMainTrigger(this.getClass(), afterALOAD_1ExecuteListener)
        );

        jvmThreadRunner.run();

    }
}