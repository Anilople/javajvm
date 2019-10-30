package com.github.anilople.javajvm.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * utils for handling byte
 * value in jvm is big end order, not small end order
 */
public class ByteUtils {

    private static final Logger logger = LoggerFactory.getLogger(ByteUtils.class);

    /**
     * convert 4 bytes to an int
     * when a byte extend to an int
     * 0xF0 -> 0xFFFFFFF0
     * if you want 0xF0 -> 0x000000F0
     * you must use ((int) b & 0xFF) manually
     * @param bytes length must be 4
     * @return
     */
    public static int bytes2int(byte[] bytes) {
        if(4 == bytes.length) {
            int value = 0xFF & ((int) bytes[0]);
            value <<= 8;
            value |= 0xFF & ((int) bytes[1]);
            value <<= 8;
            value |= 0xFF & ((int) bytes[2]);
            value <<= 8;
            value |= 0xFF & ((int) bytes[3]);
            return value;
        } else {
            logger.error("{} 's length is not 4", bytes);
            return 0;
        }
    }
}
