package com.github.anilople.javajvm.classfile.attributes;

import com.github.anilople.javajvm.classfile.ClassFile;

import java.util.Arrays;

public class ExceptionsAttribute extends AttributeInfo {

//    private short numberOfExceptions;

    /**
     * Each value in the exception_index_table array must be a valid index into
     * the constant_pool table.
     */
    private short[] exceptionIndexTable;

    public ExceptionsAttribute(ClassFile classFile, short attributeNameIndex, int attributeLength, byte[] info) {
        super(classFile, attributeNameIndex, attributeLength, info);
        this.exceptionIndexTable = ExceptionsAttribute.parseExceptionIndexTable(new ClassFile.ClassReader(info));
    }

    private static short[] parseExceptionIndexTable(ClassFile.ClassReader classReader) {
        short numberOfExceptions = classReader.readU2();
        return classReader.readShorts(numberOfExceptions);
    }

    public short[] getExceptionIndexTable() {
        return exceptionIndexTable;
    }

    @Override
    public String toString() {
        return "ExceptionsAttribute{" +
                "exceptionIndexTable=" + Arrays.toString(exceptionIndexTable) +
                '}';
    }
}