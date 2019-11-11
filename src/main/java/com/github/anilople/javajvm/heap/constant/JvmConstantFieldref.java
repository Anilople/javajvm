package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantFieldrefInfo;
import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.heap.JvmField;

public class JvmConstantFieldref extends JvmConstant {

    private ConstantFieldrefInfo constantFieldrefInfo;

    private JvmConstantFieldref() {

    }

    public JvmConstantFieldref(JvmClass jvmClass, ConstantFieldrefInfo constantFieldrefInfo) {
        super(jvmClass);
        this.constantFieldrefInfo = constantFieldrefInfo;
    }

}
