package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantDoubleInfo;
import com.github.anilople.javajvm.heap.JvmClass;

public class JvmConstantDouble extends JvmConstant {

    private ConstantDoubleInfo constantDoubleInfo;

    private JvmConstantDouble() {

    }

    public JvmConstantDouble(JvmClass jvmClass, ConstantDoubleInfo constantDoubleInfo) {
        super(jvmClass);
        this.constantDoubleInfo = constantDoubleInfo;
    }
}
