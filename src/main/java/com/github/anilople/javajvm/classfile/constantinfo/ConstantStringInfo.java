package com.github.anilople.javajvm.classfile.constantinfo;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.constants.ConstantPoolTags;

public class ConstantStringInfo extends ConstantPoolInfo {

    public static final byte TAG = ConstantPoolTags.CONSTANT_String;

    private short stringIndex;

    private ConstantStringInfo() {}

    @Override
    public byte getTag() {
        return TAG;
    }

    public ConstantStringInfo(ClassFile.ClassReader classReader) {
        this.stringIndex = classReader.readU2();
    }

    public short getStringIndex() {
        return stringIndex;
    }
}
