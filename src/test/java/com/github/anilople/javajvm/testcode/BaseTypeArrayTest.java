package com.github.anilople.javajvm.testcode;

import com.github.anilople.javajvm.JavaJvmApplication;
import com.github.anilople.javajvm.config.CommandConfig;
import org.junit.jupiter.api.Test;

/**
 * test bool, char, short, int, long
 * float, double
 */
public class BaseTypeArrayTest {

    @Test
    public void mainTest() {
        String className = this.getClass().getName();
        JavaJvmApplication.main(CommandConfig.getArgs(className));
    }

    public static void main(String[] args) {
        int[] ints = new int[5];
        ints[0] = 3;
        ints[4] = -1;
        int length = ints.length;
    }

}
