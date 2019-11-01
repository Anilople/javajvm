package com.github.anilople.javajvm.classfile.constantinfo;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.constants.ConstantPoolTags;
import com.github.anilople.javajvm.utils.ConstantPoolUtils;

public class ConstantMethodrefInfo extends ConstantPoolInfo {

    public static final byte TAG = ConstantPoolTags.CONSTANT_Methodref;

    private short classIndex;

    private short nameAndTypeIndex;

    @Override
    public byte getTag() {
        return TAG;
    }

    @Override
    public String toString() {
        ConstantPoolInfo[] constantPool = this.getClassFile().getConstantPool();
        return "ConstantMethodrefInfo{" +
                ConstantPoolUtils.getClassName(constantPool, classIndex) + "." +
                ConstantPoolUtils.getNameAndType(constantPool, this.getNameAndTypeIndex()) +
                ", classIndex=" + classIndex +
                ", nameAndTypeIndex=" + nameAndTypeIndex +
                '}';
    }

    public ConstantMethodrefInfo(ClassFile classFile, ClassFile.ClassReader classReader) {
        super(classFile);
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
