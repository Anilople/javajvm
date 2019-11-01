package com.github.anilople.javajvm.classfile.attributes;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.utils.ByteUtils;

import java.util.Arrays;

public class EnclosingMethodAttribute extends AttributeInfo {

    private short classIndex;

    private short methodIndex;

    public EnclosingMethodAttribute(ClassFile classFile, short attributeNameIndex, int attributeLength, byte[] info) {
        super(classFile, attributeNameIndex, attributeLength, info);
        this.classIndex = ByteUtils.bytes2short(Arrays.copyOfRange(info, 0, 2));
        this.methodIndex = ByteUtils.bytes2short(Arrays.copyOfRange(info, 2, 4));
    }

}
