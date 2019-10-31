package com.github.anilople.javajvm.classfile.constantinfo;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.constants.ConstantPoolTags;

public class ConstantDoubleInfo extends ConstantPoolInfo {

    public static final byte TAG = ConstantPoolTags.CONSTANT_Double;

    private int highBytes;

    private int lowBytes;

    private ConstantDoubleInfo() {}

    @Override
    public byte getTag() {
        return TAG;
    }

    public ConstantDoubleInfo(ClassFile.ClassReader classReader) {
        this.highBytes = classReader.readU4();
        this.highBytes = classReader.readU4();
    }

    public int getHighBytes() {
        return highBytes;
    }

    public int getLowBytes() {
        return lowBytes;
    }
}
