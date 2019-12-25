package com.github.anilople.javajvm.instructions.math.add;

import com.github.anilople.javajvm.heap.JvmClassLoader;
import com.github.anilople.javajvm.heap.JvmMethod;
import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectArrayReference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectReference;
import com.github.anilople.javajvm.utils.JvmMethodUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FADDTest {

    public static void main(String[] args) {
        float a = 0.3F;
        float b = 3.4F;
        float c = a + b;
    }
    
    @Test
    void execute() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(FADDTest.class));

        jvmThreadRunner.addAfterInstructionExecutionListener(
                FADD.class,
                jvmThread -> {
                    JvmClassLoader jvmClassLoader = jvmThread.currentFrame().getJvmMethod().getJvmClass().getLoader();
                    JvmMethod mainMethod = JvmMethodUtils.getMainMethod(jvmClassLoader, FADDTest.class);
                    if(jvmThread.currentFrame().getJvmMethod().equals(mainMethod)) {
                        // in the FADDTest's "main" method, so after Instruction "FADD",
                        float value = jvmThread.currentFrame().getOperandStacks().popFloatValue();
                        Assertions.assertEquals(value, 3.7F);
                        // remember that push it back
                        jvmThread.currentFrame().getOperandStacks().pushFloatValue(value);
                    }
                }
        );

        jvmThreadRunner.run();
    }
}