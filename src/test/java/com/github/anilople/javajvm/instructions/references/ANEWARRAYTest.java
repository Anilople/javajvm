package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.heap.JvmClassLoader;
import com.github.anilople.javajvm.heap.JvmMethod;
import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectArrayReference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectReference;
import com.github.anilople.javajvm.utils.JvmMethodUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ANEWARRAYTest {

    public static void main(String[] args) {
        ANEWARRAYTest[] anewarrayTests = new ANEWARRAYTest[6];
    }

    @Test
    void execute() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(ANEWARRAYTest.class));
        
        jvmThreadRunner.addAfterInstructionExecutionListener(
                ANEWARRAY.class,
                jvmThread -> {
                    JvmClassLoader jvmClassLoader = jvmThread.currentFrame().getJvmMethod().getJvmClass().getLoader();
                    JvmMethod mainMethod = JvmMethodUtils.getMainMethod(jvmClassLoader, ANEWARRAYTest.class);
                    if(jvmThread.currentFrame().getJvmMethod().equals(mainMethod)) {
                        // in the ANEWARRAYTest's "main" method, so after Instruction "ANEWARRAY",
                        ObjectArrayReference objectArrayReference = (ObjectArrayReference) jvmThread.currentFrame().getOperandStacks().popReference();
                        // the top Object on the operand stack must be the array of "ANEWARRAYTest"
                        ObjectReference typeReference = (ObjectReference) objectArrayReference.getTypeReference();
                        Assertions.assertTrue(typeReference.getJvmClass().isSameName(ANEWARRAYTest.class));
                        // remember that push it back
                        jvmThread.currentFrame().getOperandStacks().pushReference(objectArrayReference);
                    }
                }
        );

        jvmThreadRunner.run();
    }
}