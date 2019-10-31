package com.github.anilople.javajvm.classfile.attributes;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.utils.ByteUtils;

public class SourceFileAttribute extends AttributeInfo {

    private short sourceFileIndex;

    public SourceFileAttribute(ClassFile classFile, short attributeNameIndex, int attributeLength, byte[] info) {
        super(classFile, attributeNameIndex, attributeLength, info);
        this.sourceFileIndex = ByteUtils.bytes2short(info);
    }

    public short getSourceFileIndex() {
        return sourceFileIndex;
    }
}
