package com.github.anilople.javajvm.classfile.constantinfo;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.constants.ConstantPoolTags;
import com.github.anilople.javajvm.utils.ByteUtils;

public class ConstantDoubleInfo extends ConstantPoolInfo {

    public static final byte TAG = ConstantPoolTags.CONSTANT_Double;

    private int bytes;

    public ConstantDoubleInfo(ClassFile classFile, ClassFile.ClassReader classReader) {
        super(classFile);
        this.bytes = classReader.readU4();
    }

    @Override
    public byte getTag() {
        return TAG;
    }

    @Override
    public String toString() {
        return "ConstantDoubleInfo{" +
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
