package com.github.anilople.javajvm.classfile.attributes;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.utils.ByteUtils;

public class ConstantValueAttribute extends AttributeInfo {

    private short constantValueIndex;

    private ConstantValueAttribute() {
        super();
    }

    public ConstantValueAttribute(ClassFile classFile, short attributeNameIndex, int attributeLength, byte[] info) {
        super(classFile, attributeNameIndex, attributeLength, info);
        this.constantValueIndex = ByteUtils.bytes2short(info);
    }

    public short getConstantValueIndex() {
        return constantValueIndex;
    }
}
