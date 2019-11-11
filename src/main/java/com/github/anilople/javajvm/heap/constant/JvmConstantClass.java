package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantClassInfo;
import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.utils.ConstantPoolUtils;

public class JvmConstantClass extends JvmConstant {

    private ConstantClassInfo constantClassInfo;

    private JvmConstantClass() {

    }

    public JvmConstantClass(JvmClass jvmClass, ConstantClassInfo constantClassInfo) {
        super(jvmClass);
        this.constantClassInfo = constantClassInfo;
    }

    public String getName() {
        return ConstantPoolUtils.getUtf8(
                constantClassInfo.getClassFile().getConstantPool(),
                constantClassInfo.getNameIndex()
        );
    }
}
