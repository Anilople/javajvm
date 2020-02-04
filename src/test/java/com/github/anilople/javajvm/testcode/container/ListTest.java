package com.github.anilople.javajvm.testcode.container;

import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.instructions.references.ATHROW;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test of
 * @see java.util.List
 */
class ListTest {

    static void add() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(-1);
        for(Integer integer : list) {
            System.out.println(integer);
        }
    }

    @Test
    void addTest() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(
                JvmThreadFactory.createFromStaticMethod(
                        this.getClass(),
                        "add",
                        "()V"
                )
        );

        jvmThreadRunner.run();
    }

}
