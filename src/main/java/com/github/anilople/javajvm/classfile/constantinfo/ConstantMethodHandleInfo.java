package com.github.anilople.javajvm.classfile.constantinfo;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.constants.ConstantPoolTags;

public class ConstantMethodHandleInfo extends ConstantPoolInfo {

    public static final byte TAG = ConstantPoolTags.CONSTANT_MethodHandle;

    private byte tag;

    private byte referenceKind;

    private byte referenceIndex;

    private ConstantMethodHandleInfo() {}

    @Override
    public byte getTag() {
        return TAG;
    }

    public ConstantMethodHandleInfo(ClassFile.ClassReader classReader) {
        this.referenceKind = classReader.readU1();
        this.referenceIndex = classReader.readU1();
    }

    public byte getReferenceKind() {
        return referenceKind;
    }

    public byte getReferenceIndex() {
        return referenceIndex;
    }
}
