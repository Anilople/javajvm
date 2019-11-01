package com.github.anilople.javajvm.classfile.constantinfo;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.constants.ConstantPoolTags;

public class ConstantInvokeDynamicInfo extends ConstantPoolInfo {

    public static final byte TAG = ConstantPoolTags.CONSTANT_InvokeDynamic;

    private short bootstrapMethodAttrIndex;

    private short nameAndTypeIndex;

    @Override
    public byte getTag() {
        return TAG;
    }

    public ConstantInvokeDynamicInfo(ClassFile classFile, ClassFile.ClassReader classReader) {
        super(classFile);
        this.bootstrapMethodAttrIndex = classReader.readU2();
        this.nameAndTypeIndex = classReader.readU2();
    }

    public short getBootstrapMethodAttrIndex() {
        return bootstrapMethodAttrIndex;
    }

    public short getNameAndTypeIndex() {
        return nameAndTypeIndex;
    }
}
