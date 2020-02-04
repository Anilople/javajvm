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

    /**
     * concat the arrays together
     * @param arrays
     * @return
     */
    private static int[] concat(int[] ... arrays) {
        int allLength = 0;
        for(int[] ints : arrays) {
            allLength += ints.length;
        }
        final int[] afterConcatArray = new int[allLength];
        int index = 0;
        for(int[] ints : arrays) {
            for(int value : ints) {
                afterConcatArray[index++] = value;
            }
        }
        return afterConcatArray;
    }

    private static void runConcat2Array() {
        int[] a = new int[]{1, 2, 3, 4};
        int[] b = new int[]{-1, -2, -3, -4};
        int[] ab = concat(a, b);
        System.out.println(concat2String(ab, ','));
    }

    @Test
    void runConcat2ArrayTest() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(
                JvmThreadFactory.createFromStaticMethod(
                        this.getClass(),
                        "runConcat2Array",
                        "()V"
                )
        );
        jvmThreadRunner.run();
    }

    private static void runConcat3Array() {
        int[] a = new int[]{1, 2, 3, 4};
        int[] b = new int[]{5, 6};
        int[] c = new int[]{7, 8, 9};
        int[] abc = concat(a, b, c);
        System.out.println(concat2String(abc, ','));
    }

    @Test
    void runConcat3ArrayTest() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(
                JvmThreadFactory.createFromStaticMethod(
                        this.getClass(),
                        "runConcat3Array",
                        "()V"
                )
        );
        jvmThreadRunner.run();
    }

    /**
     * use quick sort algorithm to sort array
     * @param values
     * @return not increase array
     */
    private static int[] quickSort(int[] values) {
        if(values.length <= 1) {
            return values;
            // Object#clone() is not support, todo
//            return values.clone();
        }

        final int midValue = values[0];

        int smallerPartLength = 0;
        int midPartLength = 0;
        int biggerPartLength = 0;

        for(int value : values) {
            if(value < midValue) {
                smallerPartLength += 1;
            } else if(value == midValue) {
                midPartLength += 1;
            } else {
                biggerPartLength += 1;
            }
        }

        final int[] smallerPart = new int[smallerPartLength];
        final int[] midPart = new int[midPartLength];
        final int[] biggerPart = new int[biggerPartLength];

        int smallerPartIndex = 0;
        int midPartIndex = 0;
        int biggerPartIndex = 0;

        for(int value : values) {
            if(value < midValue) {
                smallerPart[smallerPartIndex++] = value;
            } else if(value == midValue) {
                midPart[midPartIndex++] = value;
            } else {
                biggerPart[biggerPartIndex++] = value;
            }
        }

        return concat(
                quickSort(smallerPart), // sort smaller part
                midPart,
                quickSort(biggerPart) // sort bigger part
        );
    }

    private static void runQuickSort() {
        final int[] values = new int[]{5, 4, 3, -1, 9, 10};
        final int[] afterSorted = quickSort(values);
        System.out.println(concat2String(afterSorted, ','));
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

    public static void main(String[] args) {
        runQuickSort();
    }
}
