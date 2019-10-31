package com.github.anilople.javajvm.classfile.attributes;

import com.github.anilople.javajvm.classfile.ClassFile;

public class LocalVariableTableAttribute extends AttributeInfo {

    public LocalVariableTableAttribute(ClassFile classFile, short attributeNameIndex, int attributeLength, byte[] info) {
        super(classFile, attributeNameIndex, attributeLength, info);
    }

}
