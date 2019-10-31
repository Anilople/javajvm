package com.github.anilople.javajvm.classfile.constantinfo;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.constants.ConstantPoolTags;

public class ConstantClassInfo extends ConstantPoolInfo {

    public static final byte TAG = ConstantPoolTags.CONSTANT_Class;

    private short nameIndex;

    @Override
    public byte getTag() {
        return TAG;
    }

    public ConstantClassInfo(ClassFile.ClassReader classReader) {
        super();
        nameIndex = classReader.readU2();
    }

    public short getNameIndex() {
        return nameIndex;
    }
}
