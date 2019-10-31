package com.github.anilople.javajvm.classfile;

import com.github.anilople.javajvm.classfile.constantinfo.ConstantPoolInfo;
import com.github.anilople.javajvm.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * The Java ® Virtual
 * Machine Specification
 * Java SE 8 Edition
 * 4.1 The ClassFile Structure
 *
 * ClassFile {
 * u4 magic;
 * u2 minor_version;
 * u2 major_version;
 * u2 constant_pool_count;
 * cp_info constant_pool[constant_pool_count-1];
 * u2 access_flags;
 * u2 this_class;
 * u2 super_class;
 * u2 interfaces_count;
 * u2 interfaces[interfaces_count];
 * u2 fields_count;
 * field_info fields[fields_count];
 * u2 methods_count;
 * method_info methods[methods_count];
 * u2 attributes_count;
 * attribute_info attributes[attributes_count];
 * }
 *
 * we use
 *  byte to represent u1
 *  short to represent u2
 *  int to represent u4
 */
public class ClassFile {

    private int magic;

    private short minorVersion;

    private short majorVersion;

//    private short constantPoolCount;

    private ConstantPoolInfo[] constantPool;

    private short accessFlags;

    private short thisClass;

    private short superClass;

//    private short interfacesCount;

    private short[] interfaces;

//    private short fieldsCount;

    private FieldInfo[] fields;

//    private short methodsCount;

    private MethodInfo[] methods;

//    private short attributesCount;

    private AttributeInfo[] attributes;

    private ClassFile() {}

    /**
     * warpper an input stream to read byte, short, int ...
     */
    public static class ClassReader {

        private static final Logger logger = LoggerFactory.getLogger(ClassReader.class);

        private InputStream inputStream;

        private ClassReader() {}

        public ClassReader(byte[] bytes) {
            this.inputStream = new ByteArrayInputStream(bytes.clone());
        }

        /**
         * given a length, read bytes which length match
         * @param length
         * @return
         */
        public byte[] readBytes(int length) {
            if(length < 0) {
                throw new RuntimeException(length + " must >= 0");
            }
            byte[] bytes = new byte[length];
            try {
                inputStream.read(bytes);
                return bytes;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public byte readU1() {
            return readBytes(1)[0];
        }

        public short readU2() {
            return ByteUtils.bytes2short(readBytes(2));
        }

        public int readU4() {
            return ByteUtils.bytes2int(readBytes(4));
        }
    }

    /**
     * parse a class file from a classReader
     * @param classReader suppose it is not null
     * @return a class file struct
     */
    public static ClassFile parse(ClassReader classReader) {

        ClassFile classFile = new ClassFile();

        classFile.magic = classReader.readU4();
        classFile.minorVersion = classReader.readU2();
        classFile.majorVersion = classReader.readU2();

        // read constant pool
        classFile.constantPool = ClassFile.parseConstantPool(classReader);

        return classFile;
    }

    /**
     * parse to constant pool
     * @param classReader
     * @return the array of constant pool info, i.e constant pool
     */
    private static ConstantPoolInfo[] parseConstantPool(ClassFile.ClassReader classReader) {
        short constantPoolCount = classReader.readU2();
        ConstantPoolInfo[] constantPool = new ConstantPoolInfo[constantPoolCount];

        // start from 1, not from 0 !!!
        for(short i = 1; i < constantPoolCount; i++) {
            constantPool[i] = ConstantPoolInfo.parseConstantPoolInfo(classReader);
        }
        return constantPool;
    }
}

