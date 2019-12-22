package com.github.anilople.javajvm.testcode;

import com.github.anilople.javajvm.JavaJvmApplication;
import com.github.anilople.javajvm.helper.CommandConfig;
import org.junit.jupiter.api.Test;

public class HelloWorldTest {

    @Test
    public void mainTest() {
        String className = this.getClass().getName();
        JavaJvmApplication.main(CommandConfig.getArgs(className));
    }

    public static void main(String[] args) {
        System.out.println("Hello, world!");
    }

}
