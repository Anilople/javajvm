package com.github.anilople.javajvm.classfile.attributes;

import com.github.anilople.javajvm.classfile.ClassFile;

public class RuntimeVisibleParameterAnnotationsAttribute extends AttributeInfo {

    public RuntimeVisibleParameterAnnotationsAttribute(ClassFile classFile, short attributeNameIndex, int attributeLength, byte[] info) {
        super(classFile, attributeNameIndex, attributeLength, info);
    }

}
