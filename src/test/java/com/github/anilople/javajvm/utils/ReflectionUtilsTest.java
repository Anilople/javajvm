package com.github.anilople.javajvm.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReflectionUtilsTest {

    private int intValue;
    private long longValue;
    private char[] chars;
    private static int staticInt;
    private double doubleValue;
    private boolean booleanValue;
    private short shortValue;

    @Test
    void getNonStaticOffset() {
        assertEquals(0, ReflectionUtils.getNonStaticOffset(this.getClass(), "intValue"));
        assertEquals(1, ReflectionUtils.getNonStaticOffset(this.getClass(), "longValue"));
        assertEquals(3, ReflectionUtils.getNonStaticOffset(this.getClass(), "chars"));
        assertEquals(4, ReflectionUtils.getNonStaticOffset(this.getClass(), "doubleValue"));
        assertEquals(6, ReflectionUtils.getNonStaticOffset(this.getClass(), "booleanValue"));
        assertEquals(7, ReflectionUtils.getNonStaticOffset(this.getClass(), "shortValue"));
    }
}