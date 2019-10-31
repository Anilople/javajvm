package com.github.anilople.javajvm.classfile.constantinfo;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.constants.ConstantPoolTags;

public class ConstantMethodrefInfo extends ConstantPoolInfo {

    public static final byte TAG = ConstantPoolTags.CONSTANT_Methodref;

    private short classIndex;

    private short nameAndTypeIndex;

    private ConstantMethodrefInfo() {}

    @Override
    public byte getTag() {
        return TAG;
    }

    public ConstantMethodrefInfo(ClassFile.ClassReader classReader) {
        this.classIndex = classReader.readU2();
        this.nameAndTypeIndex = classReader.readU2();
    }

    public short getClassIndex() {
        return classIndex;
    }

    public short getNameAndTypeIndex() {
        return nameAndTypeIndex;
    }
}
