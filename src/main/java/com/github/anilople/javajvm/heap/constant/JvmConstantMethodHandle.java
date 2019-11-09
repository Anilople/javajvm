package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantMethodHandleInfo;

public class JvmConstantMethodHandle extends JvmConstant {

    private ConstantMethodHandleInfo constantMethodHandleInfo;

    private JvmConstantMethodHandle() {

    }

    public JvmConstantMethodHandle(ConstantMethodHandleInfo constantMethodHandleInfo) {
        this.constantMethodHandleInfo = constantMethodHandleInfo;
    }
}
