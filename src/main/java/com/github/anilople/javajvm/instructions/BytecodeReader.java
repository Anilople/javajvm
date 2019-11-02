package com.github.anilople.javajvm.instructions;

import com.github.anilople.javajvm.classfile.ClassFile;

public class BytecodeReader extends ClassFile.ClassReader {

    public BytecodeReader(byte[] bytes) {
        super(bytes);
    }

}
