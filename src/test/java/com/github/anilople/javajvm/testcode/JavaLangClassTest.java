package com.github.anilople.javajvm.testcode;

import com.github.anilople.javajvm.helper.JvmThreadFactory;
import com.github.anilople.javajvm.helper.JvmThreadRunner;
import org.junit.jupiter.api.Test;

public class JavaLangClassTest {

    public static void main(String[] args) throws ClassNotFoundException {
        Class<?> charArrayClass = char[].class;
        Class<?> charClass = charArrayClass.getComponentType();
        boolean result = charClass.equals(char.class);
        System.out.println(result);
//        System.out.println(Character.class.equals(char[].class.getComponentType()));
//        System.out.println(Character.TYPE.equals(char[].class.getComponentType()));
    }

    @Test
    void mainTest() {
        JvmThreadRunner jvmThreadRunner = new JvmThreadRunner(JvmThreadFactory.makeSimpleInstance(this.getClass()));
        jvmThreadRunner.run();
    }

}
