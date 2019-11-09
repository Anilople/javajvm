package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.*;

public abstract class JvmConstant {

    public static JvmConstant generateJvmConstant(ConstantPoolInfo constantPoolInfo) {
        switch (constantPoolInfo.getTag()) {
            case ConstantClassInfo.TAG:
                return new JvmConstantClass((ConstantClassInfo) constantPoolInfo);
            case ConstantDoubleInfo.TAG:
                return new JvmConstantDouble((ConstantDoubleInfo) constantPoolInfo);
            case ConstantFieldrefInfo.TAG:
                return new JvmConstantFieldref((ConstantFieldrefInfo) constantPoolInfo);
            case ConstantFloatInfo.TAG:
                return new JvmConstantFloat((ConstantFloatInfo) constantPoolInfo);
            case ConstantIntegerInfo.TAG:
                return new JvmConstantInteger((ConstantIntegerInfo) constantPoolInfo);
            case ConstantInterfaceMethodrefInfo.TAG:
                return new JvmConstantInterfaceMethodref((ConstantInterfaceMethodrefInfo) constantPoolInfo);
            case ConstantInvokeDynamicInfo.TAG:
                return new JvmConstantInvokeDynamic((ConstantInvokeDynamicInfo) constantPoolInfo);
            case ConstantLongInfo.TAG:
                return new JvmConstantLong((ConstantLongInfo) constantPoolInfo);
            case ConstantMethodHandleInfo.TAG:
                return new JvmConstantMethodHandle((ConstantMethodHandleInfo) constantPoolInfo);
            case ConstantMethodrefInfo.TAG:
                return new JvmConstantMethodref((ConstantMethodrefInfo) constantPoolInfo);
            case ConstantMethodTypeInfo.TAG:
                return new JvmConstantMethodType((ConstantMethodTypeInfo) constantPoolInfo);
            case ConstantNameAndTypeInfo.TAG:
                return new JvmConstantNameAndType((ConstantNameAndTypeInfo) constantPoolInfo);
            case ConstantStringInfo.TAG:
                return new JvmConstantString((ConstantStringInfo) constantPoolInfo);
            case ConstantUtf8Info.TAG:
                return new JvmConstantUtf8((ConstantUtf8Info) constantPoolInfo);
            default:
                throw new IllegalStateException("Unexpected constant pool tag value: " + constantPoolInfo.getTag());
        }
    }

}
