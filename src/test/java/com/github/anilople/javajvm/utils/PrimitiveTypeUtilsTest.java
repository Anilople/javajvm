package com.github.anilople.javajvm.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrimitiveTypeUtilsTest {

    @Test
    void intFormUnsignedByteTest() {
        Assertions.assertEquals(0, PrimitiveTypeUtils.intFormUnsignedByte((byte) 0));
        Assertions.assertEquals(128, PrimitiveTypeUtils.intFormUnsignedByte(Byte.MIN_VALUE));
        Assertions.assertEquals(127, PrimitiveTypeUtils.intFormUnsignedByte(Byte.MAX_VALUE));
        Assertions.assertEquals(255, PrimitiveTypeUtils.intFormUnsignedByte(Byte.parseByte("-1")));
    }

    @Test
    void shortFormUnsignedByteTest() {
        Assertions.assertEquals((short)0, PrimitiveTypeUtils.shortFormUnsignedByte((byte) 0));
        Assertions.assertEquals((short) 128, PrimitiveTypeUtils.shortFormUnsignedByte(Byte.MIN_VALUE));
        Assertions.assertEquals((short) 127, PrimitiveTypeUtils.shortFormUnsignedByte(Byte.MAX_VALUE));
        Assertions.assertEquals((short) 255, PrimitiveTypeUtils.shortFormUnsignedByte(Byte.parseByte("-1")));
    }

    @Test
    void intFormUnsignedShortTest() {
        Assertions.assertEquals(0, PrimitiveTypeUtils.intFormUnsignedShort((short) 0));
        Assertions.assertEquals(32768, PrimitiveTypeUtils.intFormUnsignedShort(Short.MIN_VALUE));
        Assertions.assertEquals(32767, PrimitiveTypeUtils.intFormUnsignedShort(Short.MAX_VALUE));
        Assertions.assertEquals(65535, PrimitiveTypeUtils.intFormUnsignedShort((short) -1));

    }
}