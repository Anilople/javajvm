package com.github.anilople.javajvm.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * utils for handling byte
 * value in jvm is big end order, not small end order
 */
public class ByteUtils {

    private static final Logger logger = LoggerFactory.getLogger(ByteUtils.class);

    /**
     * convert 2 bytes to a short
     * when a byte extend to an short
     * 0xF0 -> 0xFFF0
     * if you want 0xF0 -> 0x00F0
     * you must use ((short) b & 0xFF) manually
     * @param bytes length must be 2
     * @return a short
     */
    public static short bytes2short(byte[] bytes) {
        if(2 != bytes.length) {
            throw new RuntimeException("bytes.length must 2");
        }
        if(2 == bytes.length) {
            int v0 = (bytes[0] & 0xFF) << 8;
            int v1 = (bytes[1] & 0xFF) ;
            short value =  (short) (v0 | v1);
            return value;
        } else {
            logger.error("{} 's length is not 2", bytes);
            return 0;
        }
    }

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
        if(4 != bytes.length) {
            throw new RuntimeException("bytes.length must 4");
        }
        int value = 0xFF & ((int) bytes[0]);
        value <<= 8;
        value |= 0xFF & ((int) bytes[1]);
        value <<= 8;
        value |= 0xFF & ((int) bytes[2]);
        value <<= 8;
        value |= 0xFF & ((int) bytes[3]);
        return value;
    }

    /**
     * convert 8 bytes to a long
     * @param bytes length must be 8
     * @return a long
     */
    public static long bytes2long(byte[] bytes) {
        if(8 != bytes.length) {
            throw new RuntimeException("bytes.length must 8");
        }
        int highValue = ByteUtils.bytes2int(Arrays.copyOfRange(bytes, 0, 4));
        int lowValue = ByteUtils.bytes2int(Arrays.copyOfRange(bytes, 4, 8));

        long value = highValue;
        value <<= 32;
        value |= lowValue;
        return value;
    }

    /**
     * convert 2 int to long
     * @param highBytes
     * @param lowBytes
     * @return
     */
    public static long int2long(int highBytes, int lowBytes) {
        long high = highBytes;
        long low = lowBytes;
        return (highBytes << 32) | lowBytes;
    }
}
