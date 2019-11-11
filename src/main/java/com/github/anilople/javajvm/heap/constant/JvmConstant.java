package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.*;
import com.github.anilople.javajvm.heap.JvmClass;

public abstract class JvmConstant {

    private JvmClass jvmClass;

    public JvmConstant() {

    }

    public JvmConstant(JvmClass jvmClass) {
        this.jvmClass = jvmClass;
    }

    public static JvmConstant generateJvmConstant(JvmClass jvmClass, ConstantPoolInfo constantPoolInfo) {
        switch (constantPoolInfo.getTag()) {
            case ConstantClassInfo.TAG:
                return new JvmConstantClass(jvmClass, (ConstantClassInfo) constantPoolInfo);
            case ConstantDoubleInfo.TAG:
                return new JvmConstantDouble(jvmClass, (ConstantDoubleInfo) constantPoolInfo);
            case ConstantFieldrefInfo.TAG:
                return new JvmConstantFieldref(jvmClass, (ConstantFieldrefInfo) constantPoolInfo);
            case ConstantFloatInfo.TAG:
                return new JvmConstantFloat(jvmClass, (ConstantFloatInfo) constantPoolInfo);
            case ConstantIntegerInfo.TAG:
                return new JvmConstantInteger(jvmClass, (ConstantIntegerInfo) constantPoolInfo);
            case ConstantInterfaceMethodrefInfo.TAG:
                return new JvmConstantInterfaceMethodref(jvmClass, (ConstantInterfaceMethodrefInfo) constantPoolInfo);
            case ConstantInvokeDynamicInfo.TAG:
                return new JvmConstantInvokeDynamic(jvmClass, (ConstantInvokeDynamicInfo) constantPoolInfo);
            case ConstantLongInfo.TAG:
                return new JvmConstantLong(jvmClass, (ConstantLongInfo) constantPoolInfo);
            case ConstantMethodHandleInfo.TAG:
                return new JvmConstantMethodHandle(jvmClass, (ConstantMethodHandleInfo) constantPoolInfo);
            case ConstantMethodrefInfo.TAG:
                return new JvmConstantMethodref(jvmClass, (ConstantMethodrefInfo) constantPoolInfo);
            case ConstantMethodTypeInfo.TAG:
                return new JvmConstantMethodType(jvmClass, (ConstantMethodTypeInfo) constantPoolInfo);
            case ConstantNameAndTypeInfo.TAG:
                return new JvmConstantNameAndType(jvmClass, (ConstantNameAndTypeInfo) constantPoolInfo);
            case ConstantStringInfo.TAG:
                return new JvmConstantString(jvmClass, (ConstantStringInfo) constantPoolInfo);
            case ConstantUtf8Info.TAG:
                return new JvmConstantUtf8(jvmClass, (ConstantUtf8Info) constantPoolInfo);
            default:
                throw new IllegalStateException("Unexpected constant pool tag value: " + constantPoolInfo.getTag());
        }
    }

    public JvmClass getJvmClass() {
        return jvmClass;
    }

}
