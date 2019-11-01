package com.github.anilople.javajvm.classfile;

import com.github.anilople.javajvm.classfile.attributes.AttributeInfo;

import java.util.Arrays;

public class FieldInfo {

    private ClassFile classFile;

    private short accessFlags;

    private short nameIndex;

    private short descriptorIndex;

//    private short attributesCount;

    private AttributeInfo[] attributes;

    private FieldInfo() {}

    public static FieldInfo[] parseFields(ClassFile classFile, ClassFile.ClassReader classReader) {
        short fieldsCount = classReader.readU2();
        FieldInfo[] fields = new FieldInfo[fieldsCount];
        for(short i = 0; i < fieldsCount; i++) {
            fields[i] = FieldInfo.parseFieldInfo(classFile, classReader);
        }
        return fields;
    }

    public static FieldInfo parseFieldInfo(ClassFile classFile, ClassFile.ClassReader classReader) {
        FieldInfo fieldInfo = new FieldInfo();
        fieldInfo.classFile = classFile;
        fieldInfo.accessFlags = classReader.readU2();
        fieldInfo.nameIndex = classReader.readU2();
        fieldInfo.descriptorIndex = classReader.readU2();
        fieldInfo.attributes = AttributeInfo.parseAttributes(classFile, classReader);
        return fieldInfo;
    }

    @Override
    public String toString() {
        return "FieldInfo{" +
                "classFile=" + classFile +
                ", accessFlags=" + accessFlags +
                ", nameIndex=" + nameIndex +
                ", descriptorIndex=" + descriptorIndex +
                ", attributes=" + Arrays.toString(attributes) +
                '}';
    }

    public ClassFile getClassFile() {
        return classFile;
    }

    public short getAccessFlags() {
        return accessFlags;
    }

    public short getNameIndex() {
        return nameIndex;
    }

    public short getDescriptorIndex() {
        return descriptorIndex;
    }

    public AttributeInfo[] getAttributes() {
        return attributes;
    }

}
