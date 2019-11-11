package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantStringInfo;
import com.github.anilople.javajvm.heap.JvmClass;

public class JvmConstantString extends JvmConstant {

    private ConstantStringInfo constantStringInfo;

    private JvmConstantString() {

    }

    public JvmConstantString(JvmClass jvmClass, ConstantStringInfo constantStringInfo) {
        super(jvmClass);
        this.constantStringInfo = constantStringInfo;
    }
}
