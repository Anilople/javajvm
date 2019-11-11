package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantMethodTypeInfo;
import com.github.anilople.javajvm.heap.JvmClass;

public class JvmConstantMethodType extends JvmConstant {

    private ConstantMethodTypeInfo constantMethodTypeInfo;

    private JvmConstantMethodType() {

    }

    public JvmConstantMethodType(JvmClass jvmClass, ConstantMethodTypeInfo constantMethodTypeInfo) {
        super(jvmClass);
        this.constantMethodTypeInfo = constantMethodTypeInfo;
    }
}
