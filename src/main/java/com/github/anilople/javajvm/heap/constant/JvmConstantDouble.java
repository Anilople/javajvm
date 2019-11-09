package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantDoubleInfo;

public class JvmConstantDouble extends JvmConstant {

    private ConstantDoubleInfo constantDoubleInfo;

    private JvmConstantDouble() {

    }

    public JvmConstantDouble(ConstantDoubleInfo constantDoubleInfo) {
        this.constantDoubleInfo = constantDoubleInfo;
    }
}
