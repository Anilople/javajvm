package com.github.anilople.javajvm.classfile.attributes;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.utils.ByteUtils;

public class SignatureAttribute extends AttributeInfo {

    private short signatureIndex;

    public SignatureAttribute(ClassFile classFile, short attributeNameIndex, int attributeLength, byte[] info) {
        super(classFile, attributeNameIndex, attributeLength, info);
        this.signatureIndex = ByteUtils.bytes2short(info);
    }

    public short getSignatureIndex() {
        return signatureIndex;
    }
}
