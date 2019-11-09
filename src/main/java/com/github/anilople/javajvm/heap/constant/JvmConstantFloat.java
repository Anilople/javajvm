package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantFloatInfo;

public class JvmConstantFloat extends JvmConstant {

    private ConstantFloatInfo constantFloatInfo;

    private JvmConstantFloat() {

    }


    public JvmConstantFloat(ConstantFloatInfo constantFloatInfo) {
        this.constantFloatInfo = constantFloatInfo;
    }
}
