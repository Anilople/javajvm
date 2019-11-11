package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantUtf8Info;
import com.github.anilople.javajvm.heap.JvmClass;

public class JvmConstantUtf8 extends JvmConstant {

    private ConstantUtf8Info constantUtf8Info;

    private JvmConstantUtf8() {

    }

    public JvmConstantUtf8(JvmClass jvmClass, ConstantUtf8Info constantUtf8Info) {
        super(jvmClass);
        this.constantUtf8Info = constantUtf8Info;
    }
}
