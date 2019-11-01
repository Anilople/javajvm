package com.github.anilople.javajvm.classfile.attributes;

import com.github.anilople.javajvm.classfile.ClassFile;

public class DeprecatedAttribute extends AttributeInfo {

    public DeprecatedAttribute(ClassFile classFile, short attributeNameIndex, int attributeLength, byte[] info) {
        super(classFile, attributeNameIndex, attributeLength, info);
    }

    @Override
    public String toString() {
        return "DeprecatedAttribute{}";
    }
}
