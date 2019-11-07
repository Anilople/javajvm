package com.github.anilople.javajvm.classfile.attributes;

import com.github.anilople.javajvm.classfile.ClassFile;

public class LineNumberTableAttribute extends AttributeInfo {

//    private short lineNumberTableLength;

    private LineNumberTableEntry[] lineNumberTable;

    public LineNumberTableAttribute(ClassFile classFile, short attributeNameIndex, int attributeLength, byte[] info) {
        super(classFile, attributeNameIndex, attributeLength, info);
        this.lineNumberTable = LineNumberTableEntry.parseLineNumberTable(new ClassFile.ClassReader(info));
    }

    public LineNumberTableEntry[] getLineNumberTable() {
        return lineNumberTable;
    }

    public static class LineNumberTableEntry {

        private short startPc;

        private short lineNumber;

        private LineNumberTableEntry() {
        }

        private LineNumberTableEntry(short startPc, short lineNumber) {
            this.startPc = startPc;
            this.lineNumber = lineNumber;
        }

        public static LineNumberTableEntry parseLineNumberTableEntry(ClassFile.ClassReader classReader) {
            return new LineNumberTableEntry(
                    classReader.readU2(),
                    classReader.readU2()
            );
        }

        public static LineNumberTableEntry[] parseLineNumberTable(ClassFile.ClassReader classReader) {
            short lineNumberTableLength = classReader.readU2();
            LineNumberTableEntry[] lineNumberTable = new LineNumberTableEntry[lineNumberTableLength];
            for (short i = 0; i < lineNumberTableLength; i++) {
                lineNumberTable[i] = LineNumberTableEntry.parseLineNumberTableEntry(classReader);
            }
            return lineNumberTable;
        }

        public short getStartPc() {
            return startPc;
        }

        public short getLineNumber() {
            return lineNumber;
        }
    }
}
