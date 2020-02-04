package com.github.anilople.javajvm.testcode;

import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.utils.ReflectionUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * write some sort algorithms,
 * use self JVM to run those algorithms,
 * check their correction.
 */
public class SortAlgorithmTest {

    private static String concat2String(int[] values, char separator) {
        StringBuilder s = new StringBuilder();
        for(int value : values) {
            s.append(value);
            s.append(separator);
        }
        return s.toString();
    }

    private static void bubbleSort(int[] values) {
        for(int i = 0; i < values.length; i++) {
            for(int j = 0; j < values.length - 1; j++) {
                if(values[j] > values[j+1]) {
                    int temp = values[j];
                    values[j] = values[j+1];
                    values[j+1] = temp;
                }
            }
        }
    }

    private static void runBubbleSort() {
        int[] values = new int[]{3, 6, 1, 2};
        bubbleSort(values);
        System.out.println(concat2String(values, ','));
    }

    @Test
    void runBubbleSortTest() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(
                JvmThreadFactory.createFromStaticMethod(
                        this.getClass(),
                        "runBubbleSort",
                        "()V"
                )
        );
        jvmThreadRunner.run();
    }

}
