package com.github.anilople.javajvm.classfile;

import com.github.anilople.javajvm.classfile.attributes.AttributeInfo;
import com.github.anilople.javajvm.classfile.constantinfo.ConstantPoolInfo;
import com.github.anilople.javajvm.utils.ByteUtils;
import com.github.anilople.javajvm.utils.ConstantPoolUtils;
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
 * <p>
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
 * <p>
 * we use
 * byte to represent u1
 * short to represent u2
 * int to represent u4
 */
public class ClassFile {

    private static final Logger logger = LoggerFactory.getLogger(ClassFile.class);

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

    private ClassFile() {
    }

    /**
     * parse a class file from a classReader
     *
     * @param classReader suppose it is not null
     * @return a class file struct
     */
    public static ClassFile parse(ClassReader classReader) {

        ClassFile classFile = new ClassFile();

        classFile.magic = classReader.readU4();
        classFile.minorVersion = classReader.readU2();
        classFile.majorVersion = classReader.readU2();

        // read constant pool
        classFile.constantPool = ConstantPoolInfo.parseConstantPool(classFile, classReader);
        logger.debug("constant pool length : {}", classFile.constantPool.length);
        for (int i = 0; i < classFile.constantPool.length; i++) {
            logger.debug("{} : {}", i, classFile.constantPool[i]);
        }
        logger.debug("parse constant pool finished.");

        classFile.accessFlags = classReader.readU2();
        classFile.thisClass = classReader.readU2();
        classFile.superClass = classReader.readU2();

        // read interfaces
        classFile.interfaces = ClassFile.parseInterfaces(classReader);
        logger.debug("parse interfaces finished. {}", classFile.interfaces);

        // read fields
        classFile.fields = FieldInfo.parseFields(classFile, classReader);
        logger.debug("parse fields finished. {}", classFile.fields);

        // read methods
        classFile.methods = MethodInfo.parseMethods(classFile, classReader);
        logger.debug("parse methods finished. {}", classFile.methods);

        // read attributes
        classFile.attributes = AttributeInfo.parseAttributes(classFile, classReader);
        logger.debug("parse attributes finished. {}", classFile.attributes);

        return classFile;
    }

    /**
     * parse interfaces from class's bytes
     *
     * @param classReader
     * @return
     */
    private static short[] parseInterfaces(ClassFile.ClassReader classReader) {
        short interfacesCount = classReader.readU2();
        return classReader.readShorts(interfacesCount);
    }

    public int getMagic() {
        return magic;
    }

    public short getMinorVersion() {
        return minorVersion;
    }

    public short getMajorVersion() {
        return majorVersion;
    }

    public ConstantPoolInfo[] getConstantPool() {
        return constantPool;
    }

    public short getAccessFlags() {
        return accessFlags;
    }

    public short getThisClass() {
        return thisClass;
    }

    public short getSuperClass() {
        return superClass;
    }

    public short[] getInterfaces() {
        return interfaces;
    }

    public FieldInfo[] getFields() {
        return fields;
    }

    public MethodInfo[] getMethods() {
        return methods;
    }

    public AttributeInfo[] getAttributes() {
        return attributes;
    }

    public String getClassName() {
        return ConstantPoolUtils.getClassName(this.getConstantPool(), this.getThisClass());
    }

    /**
     * remember that java.lang.Object have no super class,
     * so the value superClass in java.lang.Object is 0,
     * which is no mean to constant pool
     * @return
     */
    public String getSuperClassName() {
        return ConstantPoolUtils.getClassName(this.getConstantPool(), this.getSuperClass());
    }

    public boolean existSuperClass() {
        return 0 != this.superClass;
    }

    public String[] getInterfaceNames() {
        String[] interfaceNames = new String[this.getInterfaces().length];
        for(int i = 0; i < interfaceNames.length; i++) {
            interfaceNames[i] = ConstantPoolUtils.getClassName(this.getConstantPool(), this.getInterfaces()[i]);
        }
        return interfaceNames;
    }

    /**
     * warpper an input stream to read byte, short, int ...
     */
    public static class ClassReader {

        private static final Logger logger = LoggerFactory.getLogger(ClassReader.class);

        private InputStream inputStream;

        private ClassReader() {
        }

        public ClassReader(byte[] bytes) {
            this.inputStream = new ByteArrayInputStream(bytes.clone());
        }

        /**
         * given a length, read bytes which length match
         *
         * @param length
         * @return
         */
        public byte[] readBytes(int length) {
            if (length < 0) {
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

        /**
         * read shorts which's length pass by parameter
         *
         * @param length
         * @return short array
         */
        public short[] readShorts(int length) {
            short[] shorts = new short[length];
            for (int i = 0; i < length; i++) {
                short value = this.readU2();
                shorts[i] = value;
            }
            return shorts;
        }
    }
}

