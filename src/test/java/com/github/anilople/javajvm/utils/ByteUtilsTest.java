package com.github.anilople.javajvm.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ByteUtilsTest {

    @Test
    public void bytes2intTest() {
        byte[] magicBytes = {(byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE};
        Assertions.assertEquals(0xCAFEBABE, ByteUtils.bytes2int(magicBytes));
        Assertions.assertEquals(0, ByteUtils.bytes2int(new byte[2]));
        Assertions.assertEquals(0, ByteUtils.bytes2int(new byte[4]));
    }
}
