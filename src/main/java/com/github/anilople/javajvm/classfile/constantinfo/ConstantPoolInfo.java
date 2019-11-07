package com.github.anilople.javajvm.classfile.constantinfo;

import com.github.anilople.javajvm.classfile.ClassFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * all class in constant pool must inherit from this class
 */
public abstract class ConstantPoolInfo {

    private static final Logger logger = LoggerFactory.getLogger(ConstantPoolInfo.class);

    private ClassFile classFile;

    private ConstantPoolInfo() {
    }

    public ConstantPoolInfo(ClassFile classFile) {
        this.classFile = classFile;
    }

    public static ConstantPoolInfo parseConstantPoolInfo(ClassFile classFile, ClassFile.ClassReader classReader) {
        byte tag = classReader.readU1();
        switch (tag) {
            case ConstantClassInfo.TAG:
                return new ConstantClassInfo(classFile, classReader);
            case ConstantDoubleInfo.TAG:
                return new ConstantDoubleInfo(classFile, classReader);
            case ConstantFieldrefInfo.TAG:
                return new ConstantFieldrefInfo(classFile, classReader);
            case ConstantFloatInfo.TAG:
                return new ConstantFloatInfo(classFile, classReader);
            case ConstantIntegerInfo.TAG:
                return new ConstantIntegerInfo(classFile, classReader);
            case ConstantInterfaceMethodrefInfo.TAG:
                return new ConstantInterfaceMethodrefInfo(classFile, classReader);
            case ConstantInvokeDynamicInfo.TAG:
                return new ConstantInvokeDynamicInfo(classFile, classReader);
            case ConstantLongInfo.TAG:
                return new ConstantLongInfo(classFile, classReader);
            case ConstantMethodHandleInfo.TAG:
                return new ConstantMethodHandleInfo(classFile, classReader);
            case ConstantMethodrefInfo.TAG:
                return new ConstantMethodrefInfo(classFile, classReader);
            case ConstantMethodTypeInfo.TAG:
                return new ConstantMethodTypeInfo(classFile, classReader);
            case ConstantNameAndTypeInfo.TAG:
                return new ConstantNameAndTypeInfo(classFile, classReader);
            case ConstantStringInfo.TAG:
                return new ConstantStringInfo(classFile, classReader);
            case ConstantUtf8Info.TAG:
                return new ConstantUtf8Info(classFile, classReader);
            default:
                throw new IllegalStateException("Unexpected constant info tag: " + tag);
        }
    }

    /**
     * parse to constant pool
     *
     * @param classReader
     * @return the array of constant pool info, i.e constant pool
     */
    public static ConstantPoolInfo[] parseConstantPool(ClassFile classFile, ClassFile.ClassReader classReader) {
        short constantPoolCount = classReader.readU2();
        ConstantPoolInfo[] constantPool = new ConstantPoolInfo[constantPoolCount];

        // start from 1, not from 0 !!!
        for (short i = 1; i < constantPoolCount; i++) {
            constantPool[i] = ConstantPoolInfo.parseConstantPoolInfo(classFile, classReader);
            if(constantPool[i].getClass().equals(ConstantDoubleInfo.class)) {
                i += 1;
                constantPool[i] = new ConstantDoubleInfo(classFile, classReader);
            } else if(constantPool[i].getClass().equals(ConstantLongInfo.class)) {
                i += 1;
                constantPool[i] = new ConstantLongInfo(classFile, classReader);
            }
            logger.debug("read one constant pool: {}", constantPool[i]);
        }
        return constantPool;
    }

    public abstract byte getTag();

    public abstract String toString();

    public ClassFile getClassFile() {
        return classFile;
    }
}
