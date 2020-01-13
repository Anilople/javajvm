package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class INSTANCEOFTest {

    public static void main(String[] args) {
        INSTANCEOFTest instanceofTest = new INSTANCEOFTest();
        if(instanceofTest instanceof Object) {
            int a = 3;
        } else {
            int a = 666;
        }
    }

    @Test
    void execute() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.run();

        assertTrue(jvmThreadRunner.isExecuted(INSTANCEOF.class));
    }
}