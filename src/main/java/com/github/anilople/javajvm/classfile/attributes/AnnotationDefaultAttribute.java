package com.github.anilople.javajvm.classfile.attributes;

import com.github.anilople.javajvm.classfile.ClassFile;

public class AnnotationDefaultAttribute extends AttributeInfo {

    public AnnotationDefaultAttribute(ClassFile classFile, short attributeNameIndex, int attributeLength, byte[] info) {
        super(classFile, attributeNameIndex, attributeLength, info);
    }
}
