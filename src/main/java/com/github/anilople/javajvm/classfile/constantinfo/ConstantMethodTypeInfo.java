package com.github.anilople.javajvm.classfile.constantinfo;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.constants.ConstantPoolTags;

public class ConstantMethodTypeInfo extends ConstantPoolInfo {

    public static final byte TAG = ConstantPoolTags.CONSTANT_MethodType;

    private short descriptorIndex;

    public ConstantMethodTypeInfo(ClassFile classFile, ClassFile.ClassReader classReader) {
        super(classFile);
        this.descriptorIndex = classReader.readU2();
    }

    @Override
    public byte getTag() {
        return TAG;
    }

    @Override
    public String toString() {
        return "ConstantMethodTypeInfo{" +
                "descriptorIndex=" + descriptorIndex +
                '}';
    }

    public short getDescriptorIndex() {
        return descriptorIndex;
    }
}
