package com.github.anilople.javajvm.classfile.constantinfo;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.constants.ConstantPoolTags;

public class ConstantIntegerInfo extends ConstantPoolInfo {

    public static final byte TAG = ConstantPoolTags.CONSTANT_Integer;

    private int bytes;

    private ConstantIntegerInfo() {}

    @Override
    public byte getTag() {
        return TAG;
    }

    public ConstantIntegerInfo(ClassFile.ClassReader classReader) {
        this.bytes = classReader.readU4();
    }

    public int getBytes() {
        return bytes;
    }
}
