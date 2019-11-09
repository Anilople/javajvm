package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantIntegerInfo;

public class JvmConstantInteger extends JvmConstant {

    private ConstantIntegerInfo constantIntegerInfo;

    public JvmConstantInteger() {

    }

    public JvmConstantInteger(ConstantIntegerInfo constantIntegerInfo) {
        this.constantIntegerInfo = constantIntegerInfo;
    }
}
