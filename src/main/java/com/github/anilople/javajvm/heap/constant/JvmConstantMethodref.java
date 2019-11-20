package com.github.anilople.javajvm.heap.constant;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantMethodrefInfo;
import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.heap.JvmMethod;

public class JvmConstantMethodref extends JvmConstant {

    private ConstantMethodrefInfo constantMethodrefInfo;

    private JvmConstantMethodref() {

    }

    public JvmConstantMethodref(JvmClass jvmClass, ConstantMethodrefInfo constantMethodrefInfo) {
        super(jvmClass);
        this.constantMethodrefInfo = constantMethodrefInfo;
    }

    /**
     *
     * @return method name
     */
    public String getName() {
        short nameAndTypeIndex = constantMethodrefInfo.getNameAndTypeIndex();
        JvmConstantNameAndType jvmConstantNameAndType =
                (JvmConstantNameAndType) this.getJvmClass().getJvmConstantPool().getJvmConstant(nameAndTypeIndex);
        return jvmConstantNameAndType.getName();
    }

    /**
     * @return method descriptor
     */
    public String getDescriptor() {
        short nameAndTypeIndex = constantMethodrefInfo.getNameAndTypeIndex();
        JvmConstantNameAndType jvmConstantNameAndType =
                (JvmConstantNameAndType) this.getJvmClass().getJvmConstantPool().getJvmConstant(nameAndTypeIndex);
        String descriptor = jvmConstantNameAndType.getDescriptor();
        return descriptor;
    }

    /**
     * @return a jvm method
     */
    public JvmMethod resolveJvmField() {
        short nameAndTypeIndex = constantMethodrefInfo.getNameAndTypeIndex();
        JvmConstantNameAndType jvmConstantNameAndType =
                (JvmConstantNameAndType) this.getJvmClass().getJvmConstantPool().getJvmConstant(nameAndTypeIndex);
        String name = jvmConstantNameAndType.getName();
        String descriptor = jvmConstantNameAndType.getDescriptor();
        return this.getJvmClass().getMethod(name, descriptor);
    }

}
