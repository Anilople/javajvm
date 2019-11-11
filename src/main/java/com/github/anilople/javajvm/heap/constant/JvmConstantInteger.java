package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantIntegerInfo;
import com.github.anilople.javajvm.heap.JvmClass;

public class JvmConstantInteger extends JvmConstant {

    private ConstantIntegerInfo constantIntegerInfo;

    public JvmConstantInteger() {

    }

    public JvmConstantInteger(JvmClass jvmClass, ConstantIntegerInfo constantIntegerInfo) {
        super(jvmClass);
        this.constantIntegerInfo = constantIntegerInfo;
    }
}
