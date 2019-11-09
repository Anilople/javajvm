package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantStringInfo;

public class JvmConstantString extends JvmConstant {

    private ConstantStringInfo constantStringInfo;

    private JvmConstantString() {

    }

    public JvmConstantString(ConstantStringInfo constantStringInfo) {
        this.constantStringInfo = constantStringInfo;
    }
}
