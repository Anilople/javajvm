package com.github.anilople.javajvm.classfile.constantinfo;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.constants.ConstantPoolTags;
import com.github.anilople.javajvm.utils.ConstantPoolUtils;

public class ConstantStringInfo extends ConstantPoolInfo {

    public static final byte TAG = ConstantPoolTags.CONSTANT_String;

    private short stringIndex;

    public ConstantStringInfo(ClassFile classFile, ClassFile.ClassReader classReader) {
        super(classFile);
        this.stringIndex = classReader.readU2();
    }

    @Override
    public byte getTag() {
        return TAG;
    }

    @Override
    public String toString() {
        return "ConstantStringInfo{" +
                "stringIndex=" + stringIndex +
                '}';
    }

    public short getStringIndex() {
        return stringIndex;
    }
}
