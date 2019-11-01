package com.github.anilople.javajvm.classfile.constantinfo;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.constants.ConstantPoolTags;

public class ConstantNameAndTypeInfo extends ConstantPoolInfo {

    public static final byte TAG = ConstantPoolTags.CONSTANT_NameAndType;

    private short nameIndex;

    private short descriptorIndex;

    @Override
    public byte getTag() {
        return TAG;
    }

    public ConstantNameAndTypeInfo(ClassFile classFile, ClassFile.ClassReader classReader) {
        super(classFile);
        this.nameIndex = classReader.readU2();
        this.descriptorIndex = classReader.readU2();
    }

    public short getNameIndex() {
        return nameIndex;
    }

    public short getDescriptorIndex() {
        return descriptorIndex;
    }
}
