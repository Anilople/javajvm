package com.github.anilople.javajvm.classfile.constantinfo;

import com.github.anilople.javajvm.classfile.ClassFile;

public abstract class ConstantPoolInfo {

    private ClassFile classFile;

    private ConstantPoolInfo() {}

    public ConstantPoolInfo(ClassFile classFile) {
        this.classFile = classFile;
    }

    public abstract byte getTag();

    public static ConstantPoolInfo parseConstantPoolInfo(ClassFile classFile, ClassFile.ClassReader classReader) {
        byte tag = classReader.readU1();
        switch (tag) {
            case ConstantClassInfo.TAG:
                return new ConstantClassInfo(classFile, classReader);
            case ConstantDoubleInfo.TAG:
                return new ConstantDoubleInfo(classFile, classReader);
            case ConstantFieldrefInfo.TAG:
                return new ConstantFieldrefInfo(classFile, classReader);
            case ConstantFloatInfo.TAG:
                return new ConstantFloatInfo(classFile, classReader);
            case ConstantIntegerInfo.TAG:
                return new ConstantIntegerInfo(classFile, classReader);
            case ConstantInterfaceMethodrefInfo.TAG:
                return new ConstantInterfaceMethodrefInfo(classFile, classReader);
            case ConstantInvokeDynamicInfo.TAG:
                return new ConstantInvokeDynamicInfo(classFile, classReader);
            case ConstantLongInfo.TAG:
                return new ConstantLongInfo(classFile, classReader);
            case ConstantMethodHandleInfo.TAG:
                return new ConstantMethodHandleInfo(classFile, classReader);
            case ConstantMethodrefInfo.TAG:
                return new ConstantMethodrefInfo(classFile, classReader);
            case ConstantMethodTypeInfo.TAG:
                return new ConstantMethodTypeInfo(classFile, classReader);
            case ConstantNameAndTypeInfo.TAG:
                return new ConstantNameAndTypeInfo(classFile, classReader);
            case ConstantStringInfo.TAG:
                return new ConstantStringInfo(classFile, classReader);
            case ConstantUtf8Info.TAG:
                return new ConstantUtf8Info(classFile, classReader);
            default:
                throw new IllegalStateException("Unexpected constant info tag: " + tag);
        }
    }

    public abstract String toString();

    public ClassFile getClassFile() {
        return classFile;
    }
}
