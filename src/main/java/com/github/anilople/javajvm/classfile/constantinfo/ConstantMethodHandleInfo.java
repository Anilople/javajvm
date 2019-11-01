package com.github.anilople.javajvm.classfile.constantinfo;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.constants.ConstantPoolTags;

public class ConstantMethodHandleInfo extends ConstantPoolInfo {

    public static final byte TAG = ConstantPoolTags.CONSTANT_MethodHandle;

    private byte tag;

    private byte referenceKind;

    private byte referenceIndex;

    @Override
    public byte getTag() {
        return TAG;
    }

    @Override
    public String toString() {
        return "ConstantMethodHandleInfo{" +
                "tag=" + tag +
                ", referenceKind=" + referenceKind +
                ", referenceIndex=" + referenceIndex +
                '}';
    }

    public ConstantMethodHandleInfo(ClassFile classFile, ClassFile.ClassReader classReader) {
        super(classFile);
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
