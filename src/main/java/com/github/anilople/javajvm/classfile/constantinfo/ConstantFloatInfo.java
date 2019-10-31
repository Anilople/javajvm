package com.github.anilople.javajvm.classfile.constantinfo;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.constants.ConstantPoolTags;

public class ConstantFloatInfo extends ConstantPoolInfo {

    public static final byte TAG = ConstantPoolTags.CONSTANT_Float;

    private int bytes;

    private ConstantFloatInfo() {}

    @Override
    public byte getTag() {
        return TAG;
    }

    public ConstantFloatInfo(ClassFile.ClassReader classReader) {
        this.bytes = classReader.readU4();
    }
}
