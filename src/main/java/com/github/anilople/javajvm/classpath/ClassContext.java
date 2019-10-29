package com.github.anilople.javajvm.classpath;

import java.io.IOException;

public interface ClassContext {
    byte[] readClass(String className) throws IOException;
}
