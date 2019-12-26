package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantLongInfo;
import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.utils.ByteUtils;

public class JvmConstantLong extends JvmConstant {

    private ConstantLongInfo constantLongInfo;

    private JvmConstantLong() {

    }

    public JvmConstantLong(JvmClass jvmClass, ConstantLongInfo constantLongInfo) {
        super(jvmClass);
        this.constantLongInfo = constantLongInfo;
    }

    /**
     * @return long value this constant represents
     */
    public long resolveValue() {
        long value = ByteUtils.int2long(
                constantLongInfo.getHighBytes(),
                constantLongInfo.getLowBytes()
        );
        return value;
    }
}
