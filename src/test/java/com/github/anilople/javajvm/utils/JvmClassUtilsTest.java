package com.github.anilople.javajvm.utils;

import com.github.anilople.javajvm.helper.JvmClassLoaderFactory;
import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.heap.JvmClassLoader;
import com.github.anilople.javajvm.heap.JvmField;
import com.github.anilople.javajvm.heap.JvmMethod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class JvmClassUtilsTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // class loader
    private final JvmClassLoader jvmClassLoader = JvmClassLoaderFactory.getInstance();

    @Test
    void getInheritedClassesChain() {
    }

    @Test
    void getNonStaticFieldsSize() {
    }

    @Test
    void getMethodsWith() {
    }

    @Test
    void getStaticMethods() {

    }

    @Test
    void getNonStaticMethods() {
        JvmClass jvmClassUtilsTestClass = jvmClassLoader.loadClass(JvmClassUtilsTest.class);
        List<JvmMethod> nonStaticMethods = JvmClassUtils.getNonStaticMethods(jvmClassUtilsTestClass);
        logger.info("NonStaticMethods size: {}", nonStaticMethods.size());
        logger.info("real size: {} in class {}", JvmClassUtilsTest.class.getDeclaredMethods().length, JvmClassUtilsTest.class.getName());

        // the difference is static initial method "<clinit>"
        Assertions.assertEquals(
                JvmClassUtilsTest.class.getDeclaredMethods().length,
                nonStaticMethods.size() - 1
        );
    }

    @Test
    void existMethod() {
    }

    @Test
    void getMethod() {
    }

    @Test
    void getStaticFieldsSize() {
        JvmClass jvmClassUtilsTestClass = jvmClassLoader.loadClass(JvmClassUtilsTest.class);
        logger.info("jvm class name: {}", jvmClassUtilsTestClass.getName());
        Assertions.assertEquals(0, JvmClassUtils.getStaticFieldsSize(jvmClassUtilsTestClass));
    }

    @Test
    void getAllJvmFields() {
        JvmClass jvmClassUtilsTestClass = jvmClassLoader.loadClass(JvmClassUtilsTest.class);
        List<JvmField> jvmFields = JvmClassUtils.getAllJvmFields(jvmClassUtilsTestClass);
        logger.info("Jvm say {}'s field's number: {}", jvmClassUtilsTestClass.getName(), jvmFields.size());
        for(JvmField jvmField : jvmFields) {
            logger.info("field name and descriptor: {}, {}", jvmField.getName(), jvmField.getDescriptor());
        }
        Assertions.assertEquals(JvmClassUtilsTest.class.getDeclaredFields().length, JvmClassUtils.getAllJvmFields(jvmClassUtilsTestClass).size());
    }

    @Test
    void getFieldsWithAccessFlag() {
    }

    @Test
    void getStaticFields() {
    }

    @Test
    void exists() {
        JvmClass jvmClassUtilsTestClass = jvmClassLoader.loadClass(JvmClassUtilsTest.class);
        List<JvmField> jvmFields = JvmClassUtils.getAllJvmFields(jvmClassUtilsTestClass);
        logger.info("{}'s fields: {}", jvmClassUtilsTestClass.getName(), jvmFields);
        Assertions.assertTrue(
                JvmClassUtils.exists(jvmClassUtilsTestClass, jvmFields.get(0))
        );

        class A {
            private Integer vvv;
        }

        List<JvmField> aFields = JvmClassUtils.getAllJvmFields(jvmClassLoader.loadClass(A.class));
        logger.info("{}'s fields: {}", A.class.getName(), aFields);
        Assertions.assertFalse(
                JvmClassUtils.exists(jvmClassUtilsTestClass, aFields.get(0))
        );
    }

    @Test
    void existsWithAncestor() {
    }

    @Test
    void getJvmClassStaticFieldBelongTo() {
    }
}