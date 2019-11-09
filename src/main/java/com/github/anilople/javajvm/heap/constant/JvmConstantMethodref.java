package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantMethodrefInfo;

public class JvmConstantMethodref extends JvmConstant {

    private ConstantMethodrefInfo constantMethodrefInfo;

    private JvmConstantMethodref() {

    }

    public JvmConstantMethodref(ConstantMethodrefInfo constantMethodrefInfo) {
        this.constantMethodrefInfo = constantMethodrefInfo;
    }

}
