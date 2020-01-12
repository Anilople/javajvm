package com.github.anilople.javajvm.testcode;

import com.github.anilople.javajvm.JavaJvmApplication;
import com.github.anilople.javajvm.helper.CommandConfig;
import org.junit.jupiter.api.Test;

public class StringTest {

    @Test
    public void mainTest() {
        String className = this.getClass().getName();
        System.out.println(className);
        JavaJvmApplication.main(CommandConfig.getArgs(className));
    }

    public static void main(String[] args) {
        String a = "test34234";
        String b = a;
        String c = a + b;
        System.out.println(c);
    }
}
