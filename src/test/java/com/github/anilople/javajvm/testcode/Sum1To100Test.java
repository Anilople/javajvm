package com.github.anilople.javajvm.testcode;

import com.github.anilople.javajvm.JavaJvmApplication;
import com.github.anilople.javajvm.helper.CommandConfig;
import org.junit.jupiter.api.Test;

class Sum1To100Test {

    @Test
    void mainTest() {
        String className = this.getClass().getName();
        JavaJvmApplication.main(CommandConfig.getArgs(className));
    }

    public static void main(String[] args) {
        int sum = 0;
        for(int i = 1; i <= 100; i++) {
            sum +=i;
        }
    }
}