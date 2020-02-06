package com.github.anilople.javajvm.testcode.container;

import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.instructions.references.ATHROW;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test of
 * @see java.util.List
 */
class ListTest {

    public static void main(String[] args) {
        runQuickSort();
    }

    static <T> List<T> concat(List<T> ... lists) {
        List<T> all = new ArrayList<>();
        for(List<T> list : lists) {
            all.addAll(list);
        }
        return all;
    }

    static <T extends Comparable<T>> List<T> quickSort(List<T> list) {
        Objects.requireNonNull(list);
        if(list.size() <= 1) {
            return new ArrayList<>(list);
        }

        List<T> smaller = new ArrayList<>();
        List<T> equal = new ArrayList<>();
        List<T> bigger = new ArrayList<>();

        T midValue = list.get(0);
        for(T now : list) {
            if(now.compareTo(midValue) < 0) {
                smaller.add(now);
            } else if(now.compareTo(midValue) == 0) {
                equal.add(now);
            } else {
                bigger.add(now);
            }
        }

        return concat(
                quickSort(smaller),
                equal,
                quickSort(bigger)
        );
    }

    private static void runQuickSort() {
        List<Integer> list = Arrays.asList(
                5, -3, 1, 200, 0, -10, 100, 2
        );
        System.out.println("before sort: " + list);
        List<Integer> sorted = quickSort(list);
        System.out.println("after sort: " + sorted);
    }

    @Test
    void runQuickSortTest() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(
                JvmThreadFactory.createFromStaticMethod(
                        this.getClass(),
                        "runQuickSort",
                        "()V"
                )
        );

        jvmThreadRunner.run();
    }

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
