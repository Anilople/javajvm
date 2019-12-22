package com.github.anilople.javajvm.testcode;

import com.github.anilople.javajvm.JavaJvmApplication;
import com.github.anilople.javajvm.helper.CommandConfig;
import org.junit.jupiter.api.Test;

public class FibnacciTest {

    @Test
    public void mainTest() {
        String className = FibnacciTest.class.getName();
        JavaJvmApplication.main(CommandConfig.getArgs(className));
    }

    /**
     * 0 1 1 2 3
     * 5 8 13 21 34 55
     *
     * n == 0 : return 0
     * n == 1 : return 1
     * n == 2 : return 1
     * n == 3 : return 2
     * ...
     * @param n
     * @return
     */
    public static int calculateFibnacci(int n) {
        if(n <= 0) {
            return 0;
        }
        if(n <= 1) {
            return 1;
        }
        int a = 1; // n = 1
        int b = 1; // n = 2
        for(int i = 2; i < n; i++) {
            int forward = a + b;
            a = b;
            b = forward;
        }
        return b;
    }

    public static void main(String[] args) {
//        int fib0 = calculateFibnacci(0);
//        int fib1 = calculateFibnacci(1);
//        int fib2 = calculateFibnacci(2);
//        int fib3 = calculateFibnacci(3);
        int fib10 = calculateFibnacci(10);
    }
}
