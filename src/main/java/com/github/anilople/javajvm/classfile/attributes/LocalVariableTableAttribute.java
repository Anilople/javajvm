package com.github.anilople.javajvm.classfile.attributes;

import com.github.anilople.javajvm.classfile.ClassFile;

public class LocalVariableTableAttribute extends AttributeInfo {

    private LocalVariableTableEntry[] localVariableTable;

    public LocalVariableTableAttribute(ClassFile classFile, short attributeNameIndex, int attributeLength, byte[] info) {
        super(classFile, attributeNameIndex, attributeLength, info);
        localVariableTable = LocalVariableTableEntry.parseLocalVariableTable(new ClassFile.ClassReader(info));
    }

    public static class LocalVariableTableEntry {

        private short startPc;

        private short length;

        private short nameIndex;

        private short descriptorIndex;

        private short index;

        private LocalVariableTableEntry() {}

        public static LocalVariableTableEntry parseLocalVariableTableEntry(ClassFile.ClassReader classReader) {
            LocalVariableTableEntry localVariableTableEntry = new LocalVariableTableEntry();
            localVariableTableEntry.startPc = classReader.readU2();
            localVariableTableEntry.length = classReader.readU2();
            localVariableTableEntry.nameIndex = classReader.readU2();
            localVariableTableEntry.descriptorIndex = classReader.readU2();
            localVariableTableEntry.index = classReader.readU2();
            return localVariableTableEntry;
        }

        public static LocalVariableTableEntry[] parseLocalVariableTable(ClassFile.ClassReader classReader) {
            short localVariableTableLength = classReader.readU2();
            LocalVariableTableEntry[] localVariableTable = new LocalVariableTableEntry[localVariableTableLength];
            for(short i = 0; i < localVariableTableLength; i++) {
                localVariableTable[i] = parseLocalVariableTableEntry(classReader);
            }
            return localVariableTable;
        }

        public short getStartPc() {
            return startPc;
        }

        public short getLength() {
            return length;
        }

        public short getNameIndex() {
            return nameIndex;
        }

        public short getDescriptorIndex() {
            return descriptorIndex;
        }

        public short getIndex() {
            return index;
        }
    }

}

