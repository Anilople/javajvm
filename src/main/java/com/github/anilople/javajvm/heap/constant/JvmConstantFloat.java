package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantFloatInfo;
import com.github.anilople.javajvm.heap.JvmClass;

public class JvmConstantFloat extends JvmConstant {

    private ConstantFloatInfo constantFloatInfo;

    private JvmConstantFloat() {

    }


    public JvmConstantFloat(JvmClass jvmClass, ConstantFloatInfo constantFloatInfo) {
        super(jvmClass);
        this.constantFloatInfo = constantFloatInfo;
    }

    public float getFloatValue() {
        return Float.intBitsToFloat(constantFloatInfo.getBytes());
    }

}
