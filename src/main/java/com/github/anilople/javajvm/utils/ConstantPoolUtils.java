package com.github.anilople.javajvm.utils;


import com.github.anilople.javajvm.classfile.constantinfo.ConstantClassInfo;
import com.github.anilople.javajvm.classfile.constantinfo.ConstantNameAndTypeInfo;
import com.github.anilople.javajvm.classfile.constantinfo.ConstantPoolInfo;
import com.github.anilople.javajvm.classfile.constantinfo.ConstantUtf8Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConstantPoolUtils {

    private static final Logger logger = LoggerFactory.getLogger(ConstantPoolUtils.class);

    /**
     * get cp_info
     * from constant pool by index
     *
     * @param constantPool
     * @param index
     * @return
     */
    public static ConstantPoolInfo getConstantPoolInfo(ConstantPoolInfo[] constantPool, short index) {
        ConstantPoolInfo constantPoolInfo = constantPool[index];
        if (null == constantPoolInfo) {
            throw new RuntimeException("Invalid constant pool index!");
        }
        return constantPoolInfo;
    }

    /**
     * string save in
     * CONSTANT_Utf8_info
     * is encode by MUTF8(Modified UTF-8).
     *
     * @param bytes
     * @return
     */
    public static String decodeMUTF8(byte[] bytes) {
        return new String(bytes);
//        DataInput dataInput = new DataInputStream(new ByteArrayInputStream(bytes));
//        try {
//            return DataInputStream.readUTF(dataInput);
//        } catch (IOException e) {
//            logger.error("cannot decode {}", bytes);
//            throw new RuntimeException("MUTF8 decode fail", e);
//        }
    }

    /**
     * get utf8 constant with string form
     *
     * @param constantPool
     * @param index
     * @return string decode by MUTF8
     */
    public static String getUtf8(ConstantPoolInfo[] constantPool, short index) {
        ConstantPoolInfo constantPoolInfo = getConstantPoolInfo(constantPool, index);
        if (!(constantPoolInfo instanceof ConstantUtf8Info)) {
            throw new RuntimeException(index + " in constant pool is not an CONSTANT_Utf8_info");
        }
        ConstantUtf8Info constantUtf8Info = (ConstantUtf8Info) constantPoolInfo;
        return decodeMUTF8(constantUtf8Info.getBytes());
    }

    /**
     * first get CONSTANT_Class_info,
     * then by name_index in CONSTANT_Class_info,
     * find the class name
     *
     * @param constantPool
     * @param index
     * @return class name
     */
    public static String getClassName(ConstantPoolInfo[] constantPool, short index) {
        ConstantClassInfo constantClassInfo = (ConstantClassInfo) constantPool[index];
        return getUtf8(constantPool, constantClassInfo.getNameIndex());
    }

    /**
     * just get name string
     *
     * @param constantPool
     * @param constantNameAndTypeInfo
     * @return
     */
    public static String getName(ConstantPoolInfo[] constantPool, ConstantNameAndTypeInfo constantNameAndTypeInfo) {
        short nameIndex = constantNameAndTypeInfo.getNameIndex();
        return ConstantPoolUtils.getUtf8(constantPool, nameIndex);
    }

    /**
     * just get type string
     *
     * @param constantPool
     * @param constantNameAndTypeInfo
     * @return
     */
    public static String getType(ConstantPoolInfo[] constantPool, ConstantNameAndTypeInfo constantNameAndTypeInfo) {
        short descriptorIndex = constantNameAndTypeInfo.getDescriptorIndex();
        return ConstantPoolUtils.getUtf8(constantPool, descriptorIndex);
    }

    /**
     * concat name string and type string
     *
     * @param constantPool
     * @param nameAndTypeIndex
     * @return name&type
     */
    public static String getNameAndType(ConstantPoolInfo[] constantPool, short nameAndTypeIndex) {
        ConstantNameAndTypeInfo constantNameAndTypeInfo = (ConstantNameAndTypeInfo) constantPool[nameAndTypeIndex];
        String name = ConstantPoolUtils.getName(constantPool, constantNameAndTypeInfo);
        String type = ConstantPoolUtils.getType(constantPool, constantNameAndTypeInfo);
        return name + "&" + type;
    }

}
