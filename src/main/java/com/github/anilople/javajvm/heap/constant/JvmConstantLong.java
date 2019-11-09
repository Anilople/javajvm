package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantLongInfo;

public class JvmConstantLong extends JvmConstant {

    private ConstantLongInfo constantLongInfo;

    private JvmConstantLong() {

    }

    public JvmConstantLong(ConstantLongInfo constantLongInfo) {
        this.constantLongInfo = constantLongInfo;
    }
}
