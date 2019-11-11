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
}
