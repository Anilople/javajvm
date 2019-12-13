package com.github.anilople.javajvm.testcode;

import com.github.anilople.javajvm.JavaJvmApplication;
import com.github.anilople.javajvm.config.CommandConfig;
import org.junit.jupiter.api.Test;

public class ClassFieldTest {

    @Test
    public void mainTest() {
        String className = this.getClass().getName();
        JavaJvmApplication.main(CommandConfig.getArgs(className));
    }

    private int value;

    private Object object;

    public static void main(String[] args) {
        ClassFieldTest classFieldTest = new ClassFieldTest();
        classFieldTest.value = 3;
        classFieldTest.object = "";
        int a = classFieldTest.value;
    }

}
