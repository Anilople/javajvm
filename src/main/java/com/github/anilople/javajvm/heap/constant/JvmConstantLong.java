package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantLongInfo;
import com.github.anilople.javajvm.heap.JvmClass;

public class JvmConstantLong extends JvmConstant {

    private ConstantLongInfo constantLongInfo;

    private JvmConstantLong() {

    }

    public JvmConstantLong(JvmClass jvmClass, ConstantLongInfo constantLongInfo) {
        super(jvmClass);
        this.constantLongInfo = constantLongInfo;
    }
}
