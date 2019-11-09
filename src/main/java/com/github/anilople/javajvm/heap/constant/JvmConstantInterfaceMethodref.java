package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantInterfaceMethodrefInfo;

public class JvmConstantInterfaceMethodref extends JvmConstant {

    private ConstantInterfaceMethodrefInfo constantInterfaceMethodrefInfo;

    private JvmConstantInterfaceMethodref() {

    }

    public JvmConstantInterfaceMethodref(ConstantInterfaceMethodrefInfo constantInterfaceMethodrefInfo) {
        this.constantInterfaceMethodrefInfo = constantInterfaceMethodrefInfo;
    }
}
