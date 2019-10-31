package com.github.anilople.javajvm.classfile.attributes;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.classfile.constantinfo.ConstantPoolInfo;
import com.github.anilople.javajvm.utils.ConstantPoolUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AttributeInfo {

    private static final Logger logger = LoggerFactory.getLogger(AttributeInfo.class);

    private ClassFile classFile;

    private short attributeNameIndex;

    private int attributeLength;

    public AttributeInfo() {}

    public AttributeInfo(ClassFile classFile, short attributeNameIndex, int attributeLength, byte[] info) {
        this.classFile = classFile;
        this.attributeNameIndex = attributeNameIndex;
        this.attributeLength = attributeLength;
    }

    /**
     * parse multiple attributes from class reader and constant pool
     * parse attributes_count first,
     * then parse attributes_count attribute info
     * @param classFile
     * @param classReader
     * @return
     */
    public static AttributeInfo[] parseAttributes(ClassFile classFile, ClassFile.ClassReader classReader) {
        short attributesCount = classReader.readU2();
        AttributeInfo[] attributes = new AttributeInfo[attributesCount];
        for(short i = 0; i < attributesCount; i++) {
            attributes[i] = AttributeInfo.parseAttributeInfo(classFile, classReader);
        }
        return attributes;
    }

    /**
     * parse 1 attribute info from class reader and constant pool
     * @param classFile
     * @param classReader
     * @return
     */
    public static AttributeInfo parseAttributeInfo(ClassFile classFile, ClassFile.ClassReader classReader) {
        short attributeNameIndex = classReader.readU2();
        int attributeLength = classReader.readU4();
        byte[] info = classReader.readBytes(attributeLength);
        String attributeName = ConstantPoolUtils.getUtf8(classFile.getConstantPool(), attributeNameIndex);
        switch (attributeName) {
            case "ConstantValue":
                return new ConstantValueAttribute(classFile, attributeNameIndex, attributeLength, info);
            case "Code":
                return new CodeAttribute(classFile, attributeNameIndex, attributeLength, info);
            case "StackMapTable":
                return new StackMapTableAttribute(classFile, attributeNameIndex, attributeLength, info);
            case "Exceptions":
                return new ExceptionsAttribute(classFile, attributeNameIndex, attributeLength, info);
            case "InnerClasses":
                return new InnerClassesAttribute(classFile, attributeNameIndex, attributeLength, info);
            case "EnclosingMethod":
                return new EnclosingMethodAttribute(classFile, attributeNameIndex, attributeLength, info);
            case "Synthetic":
                return new SyntheticAttribute(classFile, attributeNameIndex, attributeLength, info);
            case "SourceFile":
                return new SourceFileAttribute(classFile, attributeNameIndex, attributeLength, info);
            case "SourceDebugExtension":
                return new SourceDebugExtensionAttribute(classFile, attributeNameIndex, attributeLength, info);
            case "LineNumberTable":
                return new LineNumberTableAttribute(classFile, attributeNameIndex, attributeLength, info);
            case "LocalVariableTable":
                return new LocalVariableTableAttribute(classFile, attributeNameIndex, attributeLength, info);
            case "LocalVariableTypeTable":
                return new LocalVariableTypeTableAttribute(classFile, attributeNameIndex, attributeLength, info);
            case "Deprecated":
                return new DeprecatedAttribute(classFile, attributeNameIndex, attributeLength, info);
            case "RuntimeVisibleAnnotations":
                return new RuntimeVisibleAnnotationsAttribute(classFile, attributeNameIndex, attributeLength, info);
            case "RuntimeInvisibleAnnotations":
                return new RuntimeInvisibleAnnotationsAttribute(classFile, attributeNameIndex, attributeLength, info);
            case "RuntimeVisibleParameterAnnotations":
                return new RuntimeVisibleParameterAnnotationsAttribute(classFile, attributeNameIndex, attributeLength, info);
            case "RuntimeInvisibleParameterAnnotations":
                return new RuntimeInvisibleParameterAnnotationsAttribute(classFile, attributeNameIndex, attributeLength, info);
            case "RuntimeVisibleTypeAnnotations":
                return new RuntimeVisibleTypeAnnotationsAttribute(classFile, attributeNameIndex, attributeLength, info);
            case "RuntimeInvisibleTypeAnnotations":
                return new RuntimeInvisibleTypeAnnotationsAttribute(classFile, attributeNameIndex, attributeLength, info);
            case "AnnotationDefault":
                return new AnnotationDefaultAttribute(classFile, attributeNameIndex, attributeLength, info);
            case "BootstrapMethods":
                return new BootstrapMethodsAttribute(classFile, attributeNameIndex, attributeLength, info);
            case "MethodParameters":
                return new MethodParametersAttribute(classFile, attributeNameIndex, attributeLength, info);
            default:
                logger.warn("{} is unrecognized", attributeName);
                return new UnparsedAttribute(classFile, attributeNameIndex, attributeLength, info);
        }
    }

    public ClassFile getClassFile() {
        return classFile;
    }

    public short getAttributeNameIndex() {
        return attributeNameIndex;
    }

    public int getAttributeLength() {
        return attributeLength;
    }
}
