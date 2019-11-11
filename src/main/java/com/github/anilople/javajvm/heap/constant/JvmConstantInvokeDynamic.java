package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantInvokeDynamicInfo;
import com.github.anilople.javajvm.heap.JvmClass;

public class JvmConstantInvokeDynamic extends JvmConstant {

    private ConstantInvokeDynamicInfo constantInvokeDynamicInfo;

    private JvmConstantInvokeDynamic() {

    }

    public JvmConstantInvokeDynamic(JvmClass jvmClass, ConstantInvokeDynamicInfo constantInvokeDynamicInfo) {
        super(jvmClass);
        this.constantInvokeDynamicInfo = constantInvokeDynamicInfo;
    }
}
