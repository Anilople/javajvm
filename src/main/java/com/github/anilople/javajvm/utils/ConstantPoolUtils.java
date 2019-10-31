package com.github.anilople.javajvm.utils;


import com.github.anilople.javajvm.classfile.constantinfo.ConstantClassInfo;
import com.github.anilople.javajvm.classfile.constantinfo.ConstantNameAndTypeInfo;
import com.github.anilople.javajvm.classfile.constantinfo.ConstantPoolInfo;
import com.github.anilople.javajvm.classfile.constantinfo.ConstantUtf8Info;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;

public class ConstantPoolUtils {

    /**
     * get cp_info
     * from constant pool by index
     * @param constantPool
     * @param index
     * @return
     */
    public static ConstantPoolInfo getConstantPoolInfo(ConstantPoolInfo[] constantPool, short index) {
        ConstantPoolInfo constantPoolInfo = constantPool[index];
        if(null == constantPoolInfo) {
            throw new RuntimeException("Invalid constant pool index!");
        }
        return constantPoolInfo;
    }

    /**
     * string save in
     * CONSTANT_Utf8_info
     * is encode by MUTF8(Modified UTF-8).
     * @param bytes
     * @return
     */
    public static String decodeMUTF8(byte[] bytes) {
        DataInput dataInput = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            return DataInputStream.readUTF(dataInput);
        } catch (IOException e) {
            throw new RuntimeException("MUTF8 decode fail", e);
        }
    }

    /**
     * get utf8 constant with string form
     * @param constantPool
     * @param index
     * @return string decode by MUTF8
     */
    public static String getUtf8(ConstantPoolInfo[] constantPool, short index) {
        ConstantPoolInfo constantPoolInfo = getConstantPoolInfo(constantPool, index);
        if(!(constantPoolInfo instanceof ConstantUtf8Info)) {
            throw new RuntimeException(index + " in constant pool is not an CONSTANT_Utf8_info");
        }
        ConstantUtf8Info constantUtf8Info = (ConstantUtf8Info) constantPoolInfo;
        return decodeMUTF8(constantUtf8Info.getBytes());
    }

    /**
     * first get CONSTANT_Class_info,
     * then by name_index in CONSTANT_Class_info,
     * find the class name
     * @param constantPool
     * @param index
     * @return class name
     */
    public static String getClassName(ConstantPoolInfo[] constantPool, short index) {
        ConstantClassInfo constantClassInfo = (ConstantClassInfo) constantPool[index];
        return getUtf8(constantPool, constantClassInfo.getNameIndex());
    }

//    public static getNameAndType(ConstantPoolInfo[] constantPool, short index) {
//        ConstantNameAndTypeInfo constantNameAndTypeInfo = (ConstantNameAndTypeInfo) constantPool[index];
//
//    }
}
