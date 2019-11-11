package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantMethodrefInfo;
import com.github.anilople.javajvm.heap.JvmClass;

public class JvmConstantMethodref extends JvmConstant {

    private ConstantMethodrefInfo constantMethodrefInfo;

    private JvmConstantMethodref() {

    }

    public JvmConstantMethodref(JvmClass jvmClass, ConstantMethodrefInfo constantMethodrefInfo) {
        super(jvmClass);
        this.constantMethodrefInfo = constantMethodrefInfo;
    }

}
