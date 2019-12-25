package com.github.anilople.javajvm.instructions.math.add;

import com.github.anilople.javajvm.heap.JvmClassLoader;
import com.github.anilople.javajvm.heap.JvmMethod;
import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.utils.JvmMethodUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IADDTest {

    public static void main(String[] args) {
        int i = 3;
        int j = 5;
        int c = i + j;
    }

    @Test
    void execute() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(IADDTest.class));

        jvmThreadRunner.addAfterInstructionExecutionListener(
                IADD.class,
                jvmThread -> {
                    JvmClassLoader jvmClassLoader = jvmThread.currentFrame().getJvmMethod().getJvmClass().getLoader();
                    JvmMethod mainMethod = JvmMethodUtils.getMainMethod(jvmClassLoader, IADDTest.class);
                    if(jvmThread.currentFrame().getJvmMethod().equals(mainMethod)) {
                        // there must a int number on the top of operand stack now
                        int number = jvmThread.currentFrame().getOperandStacks().popIntValue();
                        Assertions.assertEquals(8, number);
                        // remember that push it back
                        jvmThread.currentFrame().getOperandStacks().pushIntValue(number);
                    }
                }
        );

        jvmThreadRunner.run();
    }

}