package com.github.anilople.javajvm.classfile.constantinfo;

import com.github.anilople.javajvm.classfile.ClassFile;

public abstract class ConstantPoolInfo {

    public ConstantPoolInfo() {}

    public abstract byte getTag();

    public static ConstantPoolInfo parseConstantPoolInfo(ClassFile.ClassReader classReader) {
        byte tag = classReader.readU1();
        switch (tag) {
            case ConstantClassInfo.TAG:
                return new ConstantClassInfo(classReader);
            case ConstantDoubleInfo.TAG:
                return new ConstantDoubleInfo(classReader);
            case ConstantFieldrefInfo.TAG:
                return new ConstantFieldrefInfo(classReader);
            case ConstantFloatInfo.TAG:
                return new ConstantFloatInfo(classReader);
            case ConstantIntegerInfo.TAG:
                return new ConstantIntegerInfo(classReader);
            case ConstantInterfaceMethodrefInfo.TAG:
                return new ConstantInterfaceMethodrefInfo(classReader);
            case ConstantInvokeDynamicInfo.TAG:
                return new ConstantInvokeDynamicInfo(classReader);
            case ConstantLongInfo.TAG:
                return new ConstantLongInfo(classReader);
            case ConstantMethodHandleInfo.TAG:
                return new ConstantMethodHandleInfo(classReader);
            case ConstantMethodrefInfo.TAG:
                return new ConstantMethodrefInfo(classReader);
            case ConstantMethodTypeInfo.TAG:
                return new ConstantMethodTypeInfo(classReader);
            case ConstantNameAndTypeInfo.TAG:
                return new ConstantNameAndTypeInfo(classReader);
            case ConstantStringInfo.TAG:
                return new ConstantStringInfo(classReader);
            case ConstantUtf8Info.TAG:
                return new ConstantUtf8Info(classReader);
            default:
                throw new IllegalStateException("Unexpected constant info tag: " + tag);
        }
    }
}
