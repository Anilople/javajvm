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
        byte[] buffer = new byte[1024 * 1024 * 1024];
        int firstReadLen = inputStream.read(buffer);

        if(inputStream.read() >= 0) {
            // some data left in the input stream
            throw new RuntimeException("buffer is too small");
        }

        return Arrays.copyOf(buffer, firstReadLen);
    }

}
