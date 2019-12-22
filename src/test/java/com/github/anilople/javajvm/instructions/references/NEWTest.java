package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.heap.JvmClassLoader;
import com.github.anilople.javajvm.heap.JvmMethod;
import com.github.anilople.javajvm.helper.JvmClassLoaderFactory;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.JvmThread;
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
        // There is one moment after execution of Instruction new,
        // a objectref on the top of operand stack
        boolean mark = false;

        JvmClassLoader jvmClassLoader = JvmClassLoaderFactory.getInstance();
        JvmMethod jvmMethod = JvmMethodUtils.getMainMethod(jvmClassLoader, NEWTest.class);
        JvmThread jvmThread = new JvmThread();
        jvmThread.pushFrame(new Frame(jvmThread, jvmMethod));
        while (jvmThread.existFrame()) {
            Instruction instruction = jvmThread.currentFrame().readNextInstruction();
            instruction.execute(jvmThread.currentFrame());
            if(instruction instanceof NEW) {
                // after new instruction
                // maybe a target object on the top of operand stack now
                ObjectReference objectReference = (ObjectReference) jvmThread.currentFrame().getOperandStacks().popReference();
                if(objectReference.getJvmClass().isSameName(NEWTest.class)) {
                    mark = true;
                }
                // remember that push it back
                jvmThread.currentFrame().getOperandStacks().pushReference(objectReference);
            }
        }

        Assertions.assertTrue(mark);
    }
}