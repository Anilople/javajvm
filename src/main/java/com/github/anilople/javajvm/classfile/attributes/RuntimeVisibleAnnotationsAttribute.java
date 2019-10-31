package com.github.anilople.javajvm.classfile.attributes;

import com.github.anilople.javajvm.classfile.ClassFile;

public class RuntimeVisibleAnnotationsAttribute extends AttributeInfo {

    public RuntimeVisibleAnnotationsAttribute(ClassFile classFile, short attributeNameIndex, int attributeLength, byte[] info) {
        super(classFile, attributeNameIndex, attributeLength, info);
    }

}
