package com.github.anilople.javajvm.classfile.constantinfo;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.constants.ConstantPoolTags;
import com.github.anilople.javajvm.utils.ConstantPoolUtils;

public class ConstantMethodrefInfo extends ConstantPoolInfo {

    public static final byte TAG = ConstantPoolTags.CONSTANT_Methodref;

    private short classIndex;

    private short nameAndTypeIndex;

    public ConstantMethodrefInfo(ClassFile classFile, ClassFile.ClassReader classReader) {
        super(classFile);
        this.classIndex = classReader.readU2();
        this.nameAndTypeIndex = classReader.readU2();
    }

    @Override
    public byte getTag() {
        return TAG;
    }

    @Override
    public String toString() {
        return "ConstantMethodrefInfo{" +
                "classIndex=" + classIndex +
                ", nameAndTypeIndex=" + nameAndTypeIndex +
                '}';
    }

    public short getClassIndex() {
        return classIndex;
    }

    public short getNameAndTypeIndex() {
        return nameAndTypeIndex;
    }
}
