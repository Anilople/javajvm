package com.github.anilople.javajvm.classfile.constantinfo;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.constants.ConstantPoolTags;

public class ConstantLongInfo extends ConstantPoolInfo {

    public static final byte TAG = ConstantPoolTags.CONSTANT_Long;

    private int bytes;

    public ConstantLongInfo(ClassFile classFile, ClassFile.ClassReader classReader) {
        super(classFile);
        this.bytes = classReader.readU4();
    }

    @Override
    public byte getTag() {
        return TAG;
    }

    @Override
    public String toString() {
        return "ConstantLongInfo{" +
                "bytes=" + bytes +
                '}';
    }

    public static byte getTAG() {
        return TAG;
    }

    public int getBytes() {
        return bytes;
    }

}
