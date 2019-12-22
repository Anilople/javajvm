package com.github.anilople.javajvm.utils;

import com.github.anilople.javajvm.heap.JvmClassLoader;
import com.github.anilople.javajvm.heap.JvmMethod;
import com.github.anilople.javajvm.helper.JvmClassLoaderFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JvmMethodUtilsTest {

    private static final JvmClassLoader jvmClassLoader = JvmClassLoaderFactory.getInstance();

    @Test
    void getJvmMethod() {
        JvmMethod jvmMethod = JvmMethodUtils.getJvmMethod(
                jvmClassLoader,
                JvmMethodUtilsTest.class,
                "getJvmMethod",
                "()V"
        );
        assertEquals(jvmMethod.getName(), "getJvmMethod");
        assertEquals(jvmMethod.getDescriptor(), "()V");
    }


    @Test
    void getMainMethod() {
        assertThrows(
                RuntimeException.class,
                () -> JvmMethodUtils.getMainMethod(jvmClassLoader, JvmMethodUtilsTest.class)
        );
    }
}