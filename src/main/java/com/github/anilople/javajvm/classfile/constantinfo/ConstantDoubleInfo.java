package com.github.anilople.javajvm.classfile.constantinfo;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.constants.ConstantPoolTags;
import com.github.anilople.javajvm.utils.ByteUtils;

public class ConstantDoubleInfo extends ConstantPoolInfo {

    public static final byte TAG = ConstantPoolTags.CONSTANT_Double;

    private int highBytes;

    private int lowBytes;

    @Override
    public byte getTag() {
        return TAG;
    }

    @Override
    public String toString() {
        long longValue = ByteUtils.int2long(this.getHighBytes(), this.getLowBytes());
        double doubleValue = Double.longBitsToDouble(longValue);
        return Double.toString(doubleValue);
    }

    public ConstantDoubleInfo(ClassFile classFile, ClassFile.ClassReader classReader) {
        super(classFile);
        this.highBytes = classReader.readU4();
        this.lowBytes = classReader.readU4();
    }

    public int getHighBytes() {
        return highBytes;
    }

    public int getLowBytes() {
        return lowBytes;
    }
}
