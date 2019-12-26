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

class LADDTest {

    public static void main(String[] args) {
        long a = 0xFFFFFFFFFFFFL;
        long b = 0x1;
        long c = a + b;
    }
    
    @Test
    void execute() {

        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(LADDTest.class));

        jvmThreadRunner.addAfterInstructionExecutionListener(
                LADD.class,
                jvmThread -> {
                    JvmClassLoader jvmClassLoader = jvmThread.currentFrame().getJvmMethod().getJvmClass().getLoader();
                    JvmMethod mainMethod = JvmMethodUtils.getMainMethod(jvmClassLoader, LADDTest.class);
                    if(jvmThread.currentFrame().getJvmMethod().equals(mainMethod)) {
                        // in the LADDTest's "main" method, so after Instruction "LADD",
                        long value = jvmThread.currentFrame().getOperandStacks().popLongValue();
                        Assertions.assertEquals(value, 0xFFFFFFFFFFFFL + 0x1);
                        // remember that push it back
                        jvmThread.currentFrame().getOperandStacks().pushLongValue(value);
                    }
                }
        );

        jvmThreadRunner.run();
        
    }
}