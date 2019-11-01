package com.github.anilople.javajvm.classfile.constantinfo;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.constants.ConstantPoolTags;

public class ConstantStringInfo extends ConstantPoolInfo {

    public static final byte TAG = ConstantPoolTags.CONSTANT_String;

    private short stringIndex;

    @Override
    public byte getTag() {
        return TAG;
    }

    public ConstantStringInfo(ClassFile classFile, ClassFile.ClassReader classReader) {
        super(classFile);
        this.stringIndex = classReader.readU2();
    }

    public short getStringIndex() {
        return stringIndex;
    }
}
