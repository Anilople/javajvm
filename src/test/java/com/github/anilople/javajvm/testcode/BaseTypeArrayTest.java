package com.github.anilople.javajvm.testcode;

import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import com.github.anilople.javajvm.instructions.references.ARRAYLENGTH;
import com.github.anilople.javajvm.instructions.references.NEWARRAY;
import com.github.anilople.javajvm.instructions.stores.IASTORE;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test bool, char, short, int, long
 * float, double
 */
public class BaseTypeArrayTest {

    private static void intArrayPutField() {
        int[] ints = new int[5];
        ints[0] = 3;
        ints[4] = -1;
        int length = ints.length;
    }

    @Test
    void intArrayPutFieldTest() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(
                JvmThreadFactory.createFromStaticMethod(
                        this.getClass(),
                        "intArrayPutField",
                        "()V"
                )
        );

        jvmThreadRunner.run();

        // new int[]{}
        assertTrue(jvmThreadRunner.isExecuted(NEWARRAY.class));
        // ints[i] = xxx;
        assertTrue(jvmThreadRunner.isExecuted(IASTORE.class));
        // ints.length
        assertTrue(jvmThreadRunner.isExecuted(ARRAYLENGTH.class));
    }

    public static void main(String[] args) {
        intArray2String();
    }

    private static void intArray2String() {
        int[] values = new int[]{1, 2};
        String s = Arrays.toString(values);
        System.out.println(s);
    }

    @Test
    void intArray2StringTest() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(
                JvmThreadFactory.createFromStaticMethod(
                        this.getClass(),
                        "intArray2String",
                        "()V"
                )
        );

        jvmThreadRunner.run();
    }

}
