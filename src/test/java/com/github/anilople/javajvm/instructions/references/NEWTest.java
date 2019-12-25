package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.heap.JvmClassLoader;
import com.github.anilople.javajvm.heap.JvmMethod;
import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectReference;
import com.github.anilople.javajvm.utils.JvmMethodUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NEWTest {

    public static void main(String[] args) {
        NEWTest newTest = new NEWTest();
    }

    @Test
    void execute() {

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(NEWTest.class));

        jvmThreadRunner.addAfterInstructionExecutionListener(
                NEW.class,
                jvmThread -> {
                    // maybe a target object on the top of operand stack now
                    JvmClassLoader jvmClassLoader = jvmThread.currentFrame().getJvmMethod().getJvmClass().getLoader();
                    JvmMethod mainMethod = JvmMethodUtils.getMainMethod(jvmClassLoader, NEWTest.class);
                    if(jvmThread.currentFrame().getJvmMethod().equals(mainMethod)) {
                        // in the NEWTest's "main" method, so after Instruction "NEW",
                        ObjectReference objectReference = (ObjectReference) jvmThread.currentFrame().getOperandStacks().popReference();
                        // the top Object on the operand stack must be class "NEWTest"
                        Assertions.assertTrue(objectReference.getJvmClass().isSameName(NEWTest.class));
                        // remember that push it back
                        jvmThread.currentFrame().getOperandStacks().pushReference(objectReference);
                    }
                }
        );

        jvmThreadRunner.run();
    }
}