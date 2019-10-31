package com.github.anilople.javajvm.classfile.attributes;

import com.github.anilople.javajvm.classfile.ClassFile;

public class StackMapTableAttribute extends AttributeInfo {

//    private short numberOfEntries;



    public StackMapTableAttribute(ClassFile classFile, short attributeNameIndex, int attributeLength, byte[] info) {
        super(classFile, attributeNameIndex, attributeLength, info);
    }

}
