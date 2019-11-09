package com.github.anilople.javajvm.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ByteUtilsTest {

    @Test
    public void bytes2shortTest() {
        byte[] zero = new byte[2];
        Assertions.assertEquals((short) 0, ByteUtils.bytes2short(zero));
        byte[] minusOne = {(byte) 0xFF, (byte) 0xFF};
        Assertions.assertEquals((short) -1, ByteUtils.bytes2short(minusOne));
        byte[] minorVersion = {(byte) 0x00, (byte) 0x00};
        Assertions.assertEquals((short) 0, ByteUtils.bytes2short(minorVersion));
        byte[] majorVersion = {(byte) 0x00, (byte) 0x34};
        Assertions.assertEquals((short) 52, ByteUtils.bytes2short(majorVersion));

        // bytes2short(byte highByte, byte lowByte)
        Assertions.assertEquals((short) 0, ByteUtils.bytes2short((byte) 0, (byte) 0));
        Assertions.assertEquals((short) -1, ByteUtils.bytes2short((byte) 0xFF, (byte) 0xFF));
        Assertions.assertEquals((short) 52, ByteUtils.bytes2short((byte) 0x00, (byte) 0x34));
    }

    @Test
    public void bytes2intTest() {
        byte[] magicBytes = {(byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE};
        Assertions.assertEquals(0xCAFEBABE, ByteUtils.bytes2int(magicBytes));
        Assertions.assertThrows(RuntimeException.class, () -> ByteUtils.bytes2int(new byte[2]));
        Assertions.assertEquals(0, ByteUtils.bytes2int(new byte[4]));
    }

    @Test
    public void bytes2longTest() {
        byte[] firstEightBytes = {
                (byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x34
        };
        Assertions.assertEquals(
                0xCAFEBABE00000034L,
                ByteUtils.bytes2long(firstEightBytes)
        );
        byte[] zero = new byte[8];
        Assertions.assertEquals(0L, ByteUtils.bytes2long(zero));
    }

    @Test
    public void int2longTest() {
        Assertions.assertEquals(0, ByteUtils.int2long(0, 0));
        Assertions.assertEquals(-1, ByteUtils.int2long(-1, -1));
        Assertions.assertEquals(2, ByteUtils.int2long(0, 2));
        Assertions.assertEquals(Integer.MIN_VALUE, ByteUtils.int2long(0, Integer.MIN_VALUE));
    }
}
