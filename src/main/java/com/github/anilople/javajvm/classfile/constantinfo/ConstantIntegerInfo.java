package com.github.anilople.javajvm.classfile.constantinfo;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.constants.ConstantPoolTags;

public class ConstantIntegerInfo extends ConstantPoolInfo {

    public static final byte TAG = ConstantPoolTags.CONSTANT_Integer;

    private int bytes;

    public ConstantIntegerInfo(ClassFile classFile, ClassFile.ClassReader classReader) {
        super(classFile);
        this.bytes = classReader.readU4();
    }

    @Override
    public byte getTag() {
        return TAG;
    }

    @Override
    public String toString() {
        return "ConstantIntegerInfo{" +
                "bytes=" + bytes +
                '}';
    }

    public int getBytes() {
        return bytes;
    }
}
