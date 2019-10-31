package com.github.anilople.javajvm.classfile.attributes;

import com.github.anilople.javajvm.classfile.ClassFile;

public class RuntimeInvisibleTypeAnnotationsAttribute extends AttributeInfo {

    public RuntimeInvisibleTypeAnnotationsAttribute(ClassFile classFile, short attributeNameIndex, int attributeLength, byte[] info) {
        super(classFile, attributeNameIndex, attributeLength, info);
    }

}
