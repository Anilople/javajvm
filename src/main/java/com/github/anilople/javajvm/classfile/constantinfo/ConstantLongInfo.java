package com.github.anilople.javajvm.classfile.constantinfo;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.constants.ConstantPoolTags;

public class ConstantLongInfo extends ConstantPoolInfo {

    public static final byte TAG = ConstantPoolTags.CONSTANT_Long;

    private int highBytes;

    private int lowBytes;

    public ConstantLongInfo(ClassFile classFile, ClassFile.ClassReader classReader) {
        super(classFile);
        this.highBytes = classReader.readU4();
        this.lowBytes = classReader.readU4();
    }

    @Override
    public byte getTag() {
        return TAG;
    }

    @Override
    public String toString() {
        return "ConstantLongInfo{" +
                "highBytes=" + highBytes +
                ", lowBytes=" + lowBytes +
                '}';
    }
}
