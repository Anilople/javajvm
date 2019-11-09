package com.github.anilople.javajvm.classfile;

import com.github.anilople.javajvm.classfile.attributes.AttributeInfo;
import com.github.anilople.javajvm.utils.ConstantPoolUtils;

import java.util.Arrays;

public class MethodInfo {

    private ClassFile classFile;

    private short accessFlags;

    private short nameIndex;

    private short descriptorIndex;

//    private short attributesCount;

    private AttributeInfo[] attributes;

    private MethodInfo() {
    }

    private static MethodInfo parseMethodInfo(ClassFile classFile, ClassFile.ClassReader classReader) {
        MethodInfo methodInfo = new MethodInfo();
        methodInfo.classFile = classFile;
        methodInfo.accessFlags = classReader.readU2();
        methodInfo.nameIndex = classReader.readU2();
        methodInfo.descriptorIndex = classReader.readU2();
        methodInfo.attributes = AttributeInfo.parseAttributes(classFile, classReader);

        return methodInfo;
    }

    public static MethodInfo[] parseMethods(ClassFile classFile, ClassFile.ClassReader classReader) {
        short methodsCount = classReader.readU2();
        MethodInfo[] methods = new MethodInfo[methodsCount];
        for (short i = 0; i < methodsCount; i++) {
            methods[i] = MethodInfo.parseMethodInfo(classFile, classReader);
        }
        return methods;
    }

    @Override
    public String toString() {
        return "MethodInfo{" +
                "classFile=" + classFile +
                ", accessFlags=" + accessFlags +
                ", nameIndex=" + nameIndex +
                ", descriptorIndex=" + descriptorIndex +
                ", attributes=" + Arrays.toString(attributes) +
                '}';
    }

    public String getName() {
        return ConstantPoolUtils.getUtf8(this.classFile.getConstantPool(), this.nameIndex);
    }

    public String getDescriptor() {
        return ConstantPoolUtils.getUtf8(this.classFile.getConstantPool(), this.descriptorIndex);
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
