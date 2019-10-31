package com.github.anilople.javajvm.classfile.attributes;

import com.github.anilople.javajvm.classfile.ClassFile;

public class ExceptionsAttribute extends AttributeInfo {

//    private short numberOfExceptions;

    /**
     * Each value in the exception_index_table array must be a valid index into
     * the constant_pool table.
     */
    private short[] exceptionIndexTable;

    public ExceptionsAttribute(ClassFile classFile, short attributeNameIndex, int attributeLength, byte[] info) {
        super(classFile, attributeNameIndex, attributeLength, info);
        this.exceptionIndexTable = parseExceptionIndexTable(new ClassFile.ClassReader(info));
    }

    private static short[] parseExceptionIndexTable(ClassFile.ClassReader classReader) {
        short numberOfExceptions = classReader.readU2();
        return classReader.readShorts(numberOfExceptions);
    }

    public short[] getExceptionIndexTable() {
        return exceptionIndexTable;
    }
    
}