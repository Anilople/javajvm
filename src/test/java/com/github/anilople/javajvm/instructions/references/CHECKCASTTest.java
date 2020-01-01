package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.*;

class CHECKCASTTest {

    public static void main(String[] args) {
        CHECKCASTTest checkcastTest = new CHECKCASTTest();
        Object object = checkcastTest;
        checkcastTest = (CHECKCASTTest) object;

        object = null;
        checkcastTest = (CHECKCASTTest) object;
    }

    @Test
    void execute() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));

        jvmThreadRunner.run();
    }
}