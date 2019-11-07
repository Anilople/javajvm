package com.github.anilople.javajvm.classfile.attributes;

import com.github.anilople.javajvm.classfile.ClassFile;

public class LocalVariableTypeTableAttribute extends AttributeInfo {

//    private short localVariableTypeTableLength;

    LocalVariableTypeTableEntry[] localVariableTypeTable;

    public LocalVariableTypeTableAttribute(ClassFile classFile, short attributeNameIndex, int attributeLength, byte[] info) {
        super(classFile, attributeNameIndex, attributeLength, info);
        this.localVariableTypeTable = LocalVariableTypeTableEntry.parseLocalVariableTypeTable(new ClassFile.ClassReader(info));
    }


    public static class LocalVariableTypeTableEntry {

        private short startPc;

        private short length;

        private short nameIndex;

        private short signatureIndex;

        private short index;

        private LocalVariableTypeTableEntry() {
        }

        public static LocalVariableTypeTableEntry parseLocalVariableTypeTableEntry(ClassFile.ClassReader classReader) {
            LocalVariableTypeTableEntry localVariableTypeTableEntry = new LocalVariableTypeTableEntry();
            localVariableTypeTableEntry.startPc = classReader.readU2();
            localVariableTypeTableEntry.length = classReader.readU2();
            localVariableTypeTableEntry.nameIndex = classReader.readU2();
            localVariableTypeTableEntry.signatureIndex = classReader.readU2();
            localVariableTypeTableEntry.index = classReader.readU2();
            return localVariableTypeTableEntry;
        }

        public static LocalVariableTypeTableEntry[] parseLocalVariableTypeTable(ClassFile.ClassReader classReader) {
            short localVariableTypeTableLength = classReader.readU2();
            LocalVariableTypeTableEntry[] localVariableTypeTable = new LocalVariableTypeTableEntry[localVariableTypeTableLength];
            for (short i = 0; i < localVariableTypeTableLength; i++) {
                localVariableTypeTable[i] = parseLocalVariableTypeTableEntry(classReader);
            }
            return localVariableTypeTable;
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

        public short getSignatureIndex() {
            return signatureIndex;
        }

        public short getIndex() {
            return index;
        }
    }
}
