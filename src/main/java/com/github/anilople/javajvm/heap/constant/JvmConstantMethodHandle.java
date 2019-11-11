package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantMethodHandleInfo;
import com.github.anilople.javajvm.heap.JvmClass;

public class JvmConstantMethodHandle extends JvmConstant {

    private ConstantMethodHandleInfo constantMethodHandleInfo;

    private JvmConstantMethodHandle() {

    }

    public JvmConstantMethodHandle(JvmClass jvmClass, ConstantMethodHandleInfo constantMethodHandleInfo) {
        super(jvmClass);
        this.constantMethodHandleInfo = constantMethodHandleInfo;
    }
}
