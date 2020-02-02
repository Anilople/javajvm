package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantInterfaceMethodrefInfo;
import com.github.anilople.javajvm.heap.JvmClass;

public class JvmConstantInterfaceMethodref extends JvmConstant {

    private ConstantInterfaceMethodrefInfo constantInterfaceMethodrefInfo;

    private JvmConstantInterfaceMethodref() {

    }

    public JvmConstantInterfaceMethodref(JvmClass jvmClass, ConstantInterfaceMethodrefInfo constantInterfaceMethodrefInfo) {
        super(jvmClass);
        this.constantInterfaceMethodrefInfo = constantInterfaceMethodrefInfo;
    }

    /**
     * get the JvmConstantClass by class index from constant pool
     * @return
     */
    public JvmConstantClass resolveJvmConstantClass() {
        short classIndex = constantInterfaceMethodrefInfo.getClassIndex();
        JvmConstant jvmConstant = this.getJvmClass().getJvmConstantPool().getJvmConstant(classIndex);
        return (JvmConstantClass) jvmConstant;
    }

    /**
     * get the JvmConstantNameAndType by nameAndTypeIndex from constant pool
     * @return
     */
    public JvmConstantNameAndType resolveJvmConstantNameAndType() {
        short nameAndTypeIndex = constantInterfaceMethodrefInfo.getNameAndTypeIndex();
        JvmConstant jvmConstant = this.getJvmClass().getJvmConstantPool().getJvmConstant(nameAndTypeIndex);
        return (JvmConstantNameAndType) jvmConstant;
    }
}
