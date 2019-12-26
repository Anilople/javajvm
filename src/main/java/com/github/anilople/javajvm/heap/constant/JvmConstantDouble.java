package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantDoubleInfo;
import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.utils.ByteUtils;

public class JvmConstantDouble extends JvmConstant {

    private ConstantDoubleInfo constantDoubleInfo;

    private JvmConstantDouble() {

    }

    public JvmConstantDouble(JvmClass jvmClass, ConstantDoubleInfo constantDoubleInfo) {
        super(jvmClass);
        this.constantDoubleInfo = constantDoubleInfo;
    }

    public double resolveValue() {
        long longForm = ByteUtils.int2long(
                constantDoubleInfo.getHighBytes(),
                constantDoubleInfo.getLowBytes()
        );
        return Double.longBitsToDouble(longForm);
    }
}
