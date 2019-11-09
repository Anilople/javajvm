package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantInvokeDynamicInfo;

public class JvmConstantInvokeDynamic extends JvmConstant {

    private ConstantInvokeDynamicInfo constantInvokeDynamicInfo;

    private JvmConstantInvokeDynamic() {

    }

    public JvmConstantInvokeDynamic(ConstantInvokeDynamicInfo constantInvokeDynamicInfo) {
        this.constantInvokeDynamicInfo = constantInvokeDynamicInfo;
    }
}
