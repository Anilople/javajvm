package com.github.anilople.javajvm.classfile;

import com.github.anilople.javajvm.classfile.attributes.AttributeInfo;

public class MethodInfo {

    private short accessFlags;

    private short nameIndex;

    private short descriptorIndex;

    private short attributesCount;

    private AttributeInfo[] attributes;


    public static MethodInfo[] parseMethods(ClassFile classFile, ClassFile.ClassReader classReader) {
        return null;
    }


}
