package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantNameAndTypeInfo;
import com.github.anilople.javajvm.heap.JvmClass;

public class JvmConstantNameAndType extends JvmConstant {

    private ConstantNameAndTypeInfo constantNameAndTypeInfo;

    private JvmConstantNameAndType() {

    }

    public JvmConstantNameAndType(JvmClass jvmClass, ConstantNameAndTypeInfo constantNameAndTypeInfo) {
        super(jvmClass);
        this.constantNameAndTypeInfo = constantNameAndTypeInfo;
    }
}
