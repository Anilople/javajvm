package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantMethodTypeInfo;

public class JvmConstantMethodType extends JvmConstant {

    private ConstantMethodTypeInfo constantMethodTypeInfo;

    private JvmConstantMethodType() {

    }

    public JvmConstantMethodType(ConstantMethodTypeInfo constantMethodTypeInfo) {
        this.constantMethodTypeInfo = constantMethodTypeInfo;
    }
}
