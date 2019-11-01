package com.github.anilople.javajvm.classfile.attributes;

import com.github.anilople.javajvm.classfile.ClassFile;

/**
 * A StackMapTable attribute is used during the process
 * of verification by type checking (§4.10.1).
 */
public class StackMapTableAttribute extends AttributeInfo {

//    private short numberOfEntries;

    private StackMapFrame[] entries;

    public StackMapTableAttribute(ClassFile classFile, short attributeNameIndex, int attributeLength, byte[] info) {
        super(classFile, attributeNameIndex, attributeLength, info);
        this.entries = StackMapFrame.parseStackMapFrames(new ClassFile.ClassReader(info));
    }

    public static class StackMapFrame {

        private StackMapFrame() {}

        public static StackMapFrame parseStackMapFrame(ClassFile.ClassReader classReader) {
            return null;
        }

        public static StackMapFrame[] parseStackMapFrames(ClassFile.ClassReader classReader) {
            return null;
        }

    }

}
