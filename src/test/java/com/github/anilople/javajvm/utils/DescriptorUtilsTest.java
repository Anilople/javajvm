package com.github.anilople.javajvm.utils;

import com.github.anilople.javajvm.constants.Descriptors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sun.security.krb5.internal.crypto.Des;

import static org.junit.jupiter.api.Assertions.*;

class DescriptorUtilsTest {

    @Test
    void isFieldDescriptor() {
    }

    @Test
    void isFieldType() {
    }

    @Test
    void isBaseType() {
    }

    @Test
    void isObjectType() {
    }

    @Test
    void isClassName() {
    }

    @Test
    void isArrayType() {
    }

    @Test
    void isComponentType() {
    }

    @Test
    void isMethodDescriptor() {
    }

    @Test
    void isParameterDescriptor() {
    }

    @Test
    void isReturnDescriptor() {
    }

    @Test
    void isVoidDescriptor() {
    }

    @Test
    void getClassName() {
    }

    @Test
    void methodDescriptor2ParameterTypes() {
        String methodDescriptor = "(I[ZLjava/lang/String;)I";
        Class<?>[] classes = DescriptorUtils.methodDescriptor2ParameterTypes(methodDescriptor);
        assertEquals(int.class, classes[0]);
        assertEquals(boolean[].class, classes[1]);
        assertEquals(String.class, classes[2]);
    }

    @Test
    void fieldTypeDescriptor2Class() {
        assertEquals(int.class, DescriptorUtils.fieldTypeDescriptor2Class("I"));
        assertEquals(int[][][].class, DescriptorUtils.fieldTypeDescriptor2Class("[[[I"));
        assertEquals(String[].class, DescriptorUtils.fieldTypeDescriptor2Class("[Ljava/lang/String;"));

    }

    @Test
    void baseTypeDescriptor2Class() {
        assertEquals(boolean.class, DescriptorUtils.baseTypeDescriptor2Class(Descriptors.BaseType.BOOLEAN));
        assertEquals(byte.class, DescriptorUtils.baseTypeDescriptor2Class(Descriptors.BaseType.BYTE));
        assertEquals(short.class, DescriptorUtils.baseTypeDescriptor2Class(Descriptors.BaseType.SHORT));
        assertEquals(char.class, DescriptorUtils.baseTypeDescriptor2Class(Descriptors.BaseType.CHAR));
        assertEquals(int.class, DescriptorUtils.baseTypeDescriptor2Class(Descriptors.BaseType.INT));
        assertEquals(long.class, DescriptorUtils.baseTypeDescriptor2Class(Descriptors.BaseType.LONG));
        assertEquals(float.class, DescriptorUtils.baseTypeDescriptor2Class(Descriptors.BaseType.FLOAT));
        assertEquals(double.class, DescriptorUtils.baseTypeDescriptor2Class(Descriptors.BaseType.DOUBLE));
    }

    @Test
    void arrayTypeDescriptor2Class() {
        assertEquals(int[].class, DescriptorUtils.arrayTypeDescriptor2Class("[I"));
        assertEquals(int[][][].class, DescriptorUtils.arrayTypeDescriptor2Class("[[[I"));
        assertEquals(String[].class, DescriptorUtils.arrayTypeDescriptor2Class("[Ljava/lang/String;"));
    }

    @Test
    void objectTypeDescriptor2Class() {
        assertEquals(Object.class, DescriptorUtils.objectTypeDescriptor2Class("Ljava/lang/Object;"));
        assertEquals(String.class, DescriptorUtils.objectTypeDescriptor2Class("Ljava/lang/String;"));
    }
}