package com.github.anilople.javajvm.classpath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * class file warpper
 * class file mean that exist suffix ".class"
 */
public class ClassFileClassContextImpl implements ClassContext {

    private Path classfile;

    private ClassFileClassContextImpl() {}

    public ClassFileClassContextImpl(Path classfile) {
        this.classfile = classfile;
    }

    @Override
    public byte[] readClass(String className) throws IOException {
        return Files.readAllBytes(classfile);
    }
}
