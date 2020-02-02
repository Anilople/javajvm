package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class INVOKEINTERFACETest {

    public static void main(String[] args) {
        Functions functions = new FunctionsImpl();
        int result = functions.add(3, 5);
        System.out.println(result);
    }

    @Test
    void execute() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));
        jvmThreadRunner.run();
        assertTrue(jvmThreadRunner.isExecuted(INVOKEINTERFACE.class));
    }
}

interface Functions {
    int add(int a, int b);
}

class FunctionsImpl implements Functions {

    @Override
    public int add(int a, int b) {
        return a + b;
    }
}