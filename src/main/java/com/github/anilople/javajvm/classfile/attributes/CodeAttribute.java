package com.github.anilople.javajvm.classfile.attributes;

import com.github.anilople.javajvm.classfile.ClassFile;

import java.util.Arrays;

public class CodeAttribute extends AttributeInfo {

    private short maxStack;

    private short maxLocals;

//    private int codeLength;

    byte[] code;

//    private short exceptionTableLength;

    private ExceptionTableEntry[] exceptionTable;

//    private short attributesCount;

    AttributeInfo[] attributes;

    private CodeAttribute() {}

    public CodeAttribute(ClassFile classFile, short attributeNameIndex, int attributeLength, byte[] info) {
        super(classFile, attributeNameIndex, attributeLength, info);
        ClassFile.ClassReader classReader = new ClassFile.ClassReader(info);
        this.maxStack = classReader.readU2();
        this.maxLocals = classReader.readU2();
        int codeLength = classReader.readU4();
        this.code = classReader.readBytes(codeLength);
        this.exceptionTable = parseExceptionTable(classReader);
        this.attributes = AttributeInfo.parseAttributes(this.getClassFile(), classReader);
    }

    private static ExceptionTableEntry[] parseExceptionTable(ClassFile.ClassReader classReader) {
        short exceptionTableLength = classReader.readU2();
        ExceptionTableEntry[] exceptionTable = new ExceptionTableEntry[exceptionTableLength];
        for(short i = 0; i < exceptionTableLength; i++) {
            exceptionTable[i] = ExceptionTableEntry.parseExceptionTableEntry(classReader);
        }
        return exceptionTable;
    }

    @Override
    public String toString() {
        return "CodeAttribute{" +
                "maxStack=" + maxStack +
                ", maxLocals=" + maxLocals +
                ", code=" + Arrays.toString(code) +
                ", exceptionTable=" + Arrays.toString(exceptionTable) +
                ", attributes=" + Arrays.toString(attributes) +
                '}';
    }

    public short getMaxStack() {
        return maxStack;
    }

    public short getMaxLocals() {
        return maxLocals;
    }

    public byte[] getCode() {
        return code;
    }

    public ExceptionTableEntry[] getExceptionTable() {
        return exceptionTable;
    }

    public AttributeInfo[] getAttributes() {
        return attributes;
    }

    public static class ExceptionTableEntry {

        private short startPc;

        private short endPc;

        private short handlerPc;

        private short catchType;

        private ExceptionTableEntry() {}

        private ExceptionTableEntry(short startPc, short endPc, short handlerPc, short catchType) {
            this.startPc = startPc;
            this.endPc = endPc;
            this.handlerPc = handlerPc;
            this.catchType = catchType;
        }

        public static ExceptionTableEntry parseExceptionTableEntry(ClassFile.ClassReader classReader) {
            return new ExceptionTableEntry(
                    classReader.readU2(),
                    classReader.readU2(),
                    classReader.readU2(),
                    classReader.readU2()
            );
        }

        public short getStartPc() {
            return startPc;
        }

        public short getEndPc() {
            return endPc;
        }

        public short getHandlerPc() {
            return handlerPc;
        }

        public short getCatchType() {
            return catchType;
        }

    }
}
