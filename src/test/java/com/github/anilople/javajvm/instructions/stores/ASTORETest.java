package com.github.anilople.javajvm.instructions.stores;

import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

class ASTORETest {

    public static void main(String[] args) {
        Object v0 = null;
        Object v1 = v0;
        Object v2 = v1;
        Object v3 = v2;
        Object v4 = v3;

        Object r0 = new Object();
        Object r1 = r0;
        Object r2 = r1;
        Object r3 = r2;
        Object r4 = r3;
    }

    private Reference reference = null;

    @Test
    void execute() {

        final BiConsumer<Instruction, JvmThread> beforeASTOREExecuteListener = (instruction, jvmThread) -> {
            assertTrue(instruction instanceof ASTORE);
            Reference fromOperandStack = jvmThread.currentFrame().getOperandStacks().popReference();
            this.reference = fromOperandStack;
            // push it back because the stack need balance
            jvmThread.currentFrame().getOperandStacks().pushReference(fromOperandStack);
        };

        final BiConsumer<Instruction, JvmThread> afterASTOREExecuteListener = (instruction, jvmThread) -> {
            assertNotNull(reference);
            ASTORE astore = (ASTORE) instruction;
            int index = astore.resolveIndex();
            Reference fromLocalVariable = jvmThread.currentFrame().getLocalVariables().getReference(index);
            // they must be equal
            assertEquals(reference, fromLocalVariable);
        };

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.addBeforeInstructionExecutionListener(
                ASTORE.class,
                beforeASTOREExecuteListener
        );

        jvmThreadRunner.addAfterInstructionExecutionListener(
                ASTORE.class,
                afterASTOREExecuteListener
        );

        jvmThreadRunner.addEndListener(jvmThread -> assertNotNull(this.reference));

        jvmThreadRunner.run();
    }
}