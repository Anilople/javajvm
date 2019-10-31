package com.github.anilople.javajvm.classfile.constantinfo;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.constants.ConstantPoolTags;

public class ConstantLongInfo extends ConstantPoolInfo {

    public static final byte TAG = ConstantPoolTags.CONSTANT_Long;

    private int highBytes;

    private int lowBytes;

    private ConstantLongInfo() {}

    @Override
    public byte getTag() {
        return TAG;
    }

    public ConstantLongInfo(ClassFile.ClassReader classReader) {
        this.highBytes = classReader.readU4();
        this.lowBytes = classReader.readU4();
    }

    public int getHighBytes() {
        return highBytes;
    }

    public int getLowBytes() {
        return lowBytes;
    }
}
