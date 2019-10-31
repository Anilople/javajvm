package com.github.anilople.javajvm.classfile.constantinfo;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.constants.ConstantPoolTags;

public class ConstantMethodTypeInfo extends ConstantPoolInfo {

    public static final byte TAG = ConstantPoolTags.CONSTANT_MethodType;

    private short descriptorIndex;

    private ConstantMethodTypeInfo() {}

    @Override
    public byte getTag() {
        return TAG;
    }

    public ConstantMethodTypeInfo(ClassFile.ClassReader classReader) {
        this.descriptorIndex = classReader.readU2();
    }

    public short getDescriptorIndex() {
        return descriptorIndex;
    }
}
