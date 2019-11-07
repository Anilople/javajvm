package com.github.anilople.javajvm.classfile.constantinfo;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.constants.ConstantPoolTags;
import com.github.anilople.javajvm.utils.ConstantPoolUtils;

public class ConstantClassInfo extends ConstantPoolInfo {

    public static final byte TAG = ConstantPoolTags.CONSTANT_Class;

    /**
     * The value of the name_index item must be a valid index into the
     * constant_pool table. The constant_pool entry at that index must be a
     * CONSTANT_Utf8_info structure (§4.4.7) representing a valid binary class or
     * interface name encoded in internal form (§4.2.1)
     */
    private short nameIndex;

    public ConstantClassInfo(ClassFile classFile, ClassFile.ClassReader classReader) {
        super(classFile);
        nameIndex = classReader.readU2();
    }

    @Override
    public byte getTag() {
        return TAG;
    }

    @Override
    public String toString() {
        return ConstantPoolUtils.getUtf8(this.getClassFile().getConstantPool(), this.getNameIndex());
    }

    public short getNameIndex() {
        return nameIndex;
    }
}
