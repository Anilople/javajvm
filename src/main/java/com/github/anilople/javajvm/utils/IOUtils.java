package com.github.anilople.javajvm.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * for the io
 */
public class IOUtils {

    /**
     * read n bytes from input stream (or meet EOF)
     *
     * @param inputStream
     * @param n
     * @return
     */
    public static byte[] readNBytes(InputStream inputStream, int n) throws IOException {
        byte[] target = new byte[128 * 1024 * 1024];
        int len = 0;

        byte[] buffer = new byte[1024 * 1024];
        for(int nowLen = inputStream.read(buffer);
            -1 != nowLen && len < n;
            nowLen = inputStream.read(buffer)
        ) {
            while(len + nowLen >= target.length) {
                // grow target bytes
                byte[] newTarget = new byte[target.length << 1];
                System.arraycopy(target, 0, newTarget, 0, len);
                target = newTarget;
            }
            // copy buffer's bytes to target
            System.arraycopy(buffer, 0, target, len, nowLen);
            // add nowLen to len
            len += nowLen;
        }

        return Arrays.copyOf(target, len);
    }

}
