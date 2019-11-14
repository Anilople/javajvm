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

    /**
     * get name by run-time constant pool and name index
     * @return
     */
    public String getName() {
        // name
        int nameIndex = constantNameAndTypeInfo.getNameIndex();
        String name = this.getJvmClass().getJvmConstantPool().getUtf8String(nameIndex);
        return name;
    }

    /**
     * get type string by run-time constant pool and descriptor index
     * @return
     */
    public String getDescriptor() {
        int descriptorIndex = constantNameAndTypeInfo.getDescriptorIndex();
        String descriptor = this.getJvmClass().getJvmConstantPool().getUtf8String(descriptorIndex);
        return descriptor;
    }
}
