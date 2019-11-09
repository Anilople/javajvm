package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantNameAndTypeInfo;

public class JvmConstantNameAndType extends JvmConstant {

    private ConstantNameAndTypeInfo constantNameAndTypeInfo;

    private JvmConstantNameAndType() {

    }

    public JvmConstantNameAndType(ConstantNameAndTypeInfo constantNameAndTypeInfo) {
        this.constantNameAndTypeInfo = constantNameAndTypeInfo;
    }
}
