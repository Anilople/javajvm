package com.github.anilople.javajvm.classfile.attributes;

import com.github.anilople.javajvm.classfile.ClassFile;

/**
 * the attribute which jvm cannot recognize
 */
public class UnparsedAttribute extends AttributeInfo {

    private byte[] info;

    private UnparsedAttribute() {}

    public UnparsedAttribute(ClassFile classFile, short attributeNameIndex, int attributeLength, byte[] info) {
        super(classFile, attributeNameIndex, attributeLength, info);
        this.info = info;
    }
}
