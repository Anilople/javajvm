package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantFieldrefInfo;

public class JvmConstantFieldref extends JvmConstant {

    private ConstantFieldrefInfo constantFieldrefInfo;

    private JvmConstantFieldref() {

    }

    public JvmConstantFieldref(ConstantFieldrefInfo constantFieldrefInfo) {
        this.constantFieldrefInfo = constantFieldrefInfo;
    }
}
