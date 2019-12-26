package com.github.anilople.javajvm.classfile.constantinfo;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.constants.ConstantPoolTags;
import com.github.anilople.javajvm.utils.ByteUtils;

public class ConstantDoubleInfo extends ConstantPoolInfo {

    public static final byte TAG = ConstantPoolTags.CONSTANT_Double;

    private int highBytes;

    private int lowBytes;

    public ConstantDoubleInfo(ClassFile classFile, ClassFile.ClassReader classReader) {
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
        return "ConstantDoubleInfo{" +
                "highBytes=" + highBytes +
                ", lowBytes=" + lowBytes +
                '}';
    }

    public int getHighBytes() {
        return highBytes;
    }

    public int getLowBytes() {
        return lowBytes;
    }
}
