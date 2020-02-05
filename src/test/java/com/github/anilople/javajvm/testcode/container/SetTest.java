package com.github.anilople.javajvm.testcode.container;

import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * test Set in self jvm
 */
public class SetTest {

    private static void add() {
        Set<String> strings = new HashSet<>();
        strings.add("abc");
        strings.add("123");
        strings.add("666");
        for(String s : strings) {
            System.out.println(s);
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

    /**
     * Example code
     * use set to remove duplicate elements in array
     */
    private static void useSetToRemoveDuplicate() {
        int[] values = new int[]{1, 1, 2, 3, 5, 2, 6, -1, 5};
        Set<Integer> integers = new TreeSet<>();
        for(int value : values) {
            integers.add(value);
        }
        System.out.println("After remove duplicate elements:");

        for(Integer i : integers) {
            System.out.println(i);
        }
    }

    @Test
    void useSetToRemoveDuplicateTest() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(
                JvmThreadFactory.createFromStaticMethod(
                        this.getClass(),
                        "useSetToRemoveDuplicate",
                        "()V"
                )
        );

        jvmThreadRunner.run();
    }
}
