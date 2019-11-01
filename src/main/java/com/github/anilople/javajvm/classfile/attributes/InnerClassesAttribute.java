package com.github.anilople.javajvm.classfile.attributes;

import com.github.anilople.javajvm.classfile.ClassFile;

public class InnerClassesAttribute extends AttributeInfo {

//    private short numberOfClasses;

    ClassesEntry[] classes;

    public InnerClassesAttribute(ClassFile classFile, short attributeNameIndex, int attributeLength, byte[] info) {
        super(classFile, attributeNameIndex, attributeLength, info);
        this.classes = ClassesEntry.parseClasses(new ClassFile.ClassReader(info));
    }

    public ClassesEntry[] getClasses() {
        return classes;
    }

    public static class ClassesEntry {

        private short innerClassInfoIndex;

        private short outerClassInfoIndex;

        private short innerNameIndex;

        private short innerClassAccessFlags;

        private ClassesEntry() {}

        private ClassesEntry(short innerClassInfoIndex, short outerClassInfoIndex, short innerNameIndex, short innerClassAccessFlags) {
            this.innerClassInfoIndex = innerClassInfoIndex;
            this.outerClassInfoIndex = outerClassInfoIndex;
            this.innerNameIndex = innerNameIndex;
            this.innerClassAccessFlags = innerClassAccessFlags;
        }

        public static ClassesEntry parseClassesEntry(ClassFile.ClassReader classReader) {
            ClassesEntry classesEntry = new ClassesEntry();
            classesEntry.innerClassInfoIndex = classReader.readU2();
            classesEntry.outerClassInfoIndex = classReader.readU2();
            classesEntry.innerNameIndex = classReader.readU2();
            classesEntry.innerClassAccessFlags = classReader.readU2();
            return classesEntry;
        }

        public static ClassesEntry[] parseClasses(ClassFile.ClassReader classReader) {
            short numberOfClasses = classReader.readU2();
            ClassesEntry[] classes = new ClassesEntry[numberOfClasses];
            for(short i = 0; i < numberOfClasses; i++) {
                classes[i] = parseClassesEntry(classReader);
            }
            return classes;
        }

        public short getInnerClassInfoIndex() {
            return innerClassInfoIndex;
        }

        public short getOuterClassInfoIndex() {
            return outerClassInfoIndex;
        }

        public short getInnerNameIndex() {
            return innerNameIndex;
        }

        public short getInnerClassAccessFlags() {
            return innerClassAccessFlags;
        }
    }
}
