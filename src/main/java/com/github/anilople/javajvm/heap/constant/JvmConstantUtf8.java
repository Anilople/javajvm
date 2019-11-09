package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantUtf8Info;

public class JvmConstantUtf8 extends JvmConstant {

    private ConstantUtf8Info constantUtf8Info;

    private JvmConstantUtf8() {

    }

    public JvmConstantUtf8(ConstantUtf8Info constantUtf8Info) {
        this.constantUtf8Info = constantUtf8Info;
    }
}
