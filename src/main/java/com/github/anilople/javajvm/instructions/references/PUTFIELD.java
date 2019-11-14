package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.constants.Descriptors;
import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.heap.JvmConstantPool;
import com.github.anilople.javajvm.heap.JvmField;
import com.github.anilople.javajvm.heap.constant.JvmConstantFieldref;
import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectReference;
import com.github.anilople.javajvm.utils.ByteUtils;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Operation
 *      Set field in object
 *
 * Operand ..., objectref, value →
 * Stack ...
 *
 * Description:
 * The run-time constant pool item at that index must be a symbolic
 * reference to a field (§5.1), which gives the name and descriptor of
 * the field as well as a symbolic reference to the class in which the
 * field is to be found. The class of objectref must not be an array.
 */
public class PUTFIELD implements Instruction {

    private static final Logger logger = LoggerFactory.getLogger(PUTFIELD.class);

    private byte indexByte1;

    private byte indexByte2;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.indexByte1 = bytecodeReader.readU1();
        this.indexByte2 = bytecodeReader.readU1();
    }

    @Override
    public int execute(Frame frame) {
        JvmClass jvmClass = frame.getJvmMethod().getJvmClass();
        JvmConstantPool jvmConstantPool = jvmClass.getJvmConstantPool();
        int index = PrimitiveTypeUtils.intFormUnsignedShort(ByteUtils.bytes2short(indexByte1, indexByte2));
        JvmConstantFieldref jvmConstantFieldref = (JvmConstantFieldref) jvmConstantPool.getJvmConstant(index);

        JvmField jvmField = jvmConstantFieldref.resolveJvmField();
        logger.debug("jvm field: {}", jvmField);

        // linking exceptions
        if(jvmField.isStatic()) {
            /**
             * Otherwise, if the resolved field is a static field, putfield throws
             * an IncompatibleClassChangeError
             */
            throw new IncompatibleClassChangeError("bytecode putfield cannot put a static field" + jvmConstantFieldref);
        } else if(jvmField.isFinal()) {
            /**
             * Otherwise, if the field is final , it must be declared in the
             * current class, and the instruction must occur in an instance
             * initialization method ( <init> ) of the current class. Otherwise, an
             * IllegalAccessError is thrown.
             */

            if(!jvmConstantFieldref.getClassName().equals(jvmClass.getName())) {
                // field not declared in the current class
                throw new IllegalAccessError(jvmConstantFieldref + "is final, but not in current class " + jvmClass.getName());
            }

            if(!"<init>".equals(frame.getJvmMethod().getName())) {
                // the instruction must occur in an instance
                // initialization method ( <init> ) of the current class.
                throw new IllegalAccessError(frame.getJvmMethod().getName() + " is not <init>");
            }
        }

        String descriptor = jvmConstantFieldref.getFieldDescriptor();
        logger.debug("descriptor: {}", descriptor);

        switch (descriptor) {
            case Descriptors.BaseType.BOOLEAN: // boolean
            case Descriptors.BaseType.BYTE: // byte
            case Descriptors.BaseType.CHAR: // char
            case Descriptors.BaseType.SHORT: // short
            case Descriptors.BaseType.INT:
                int intValue = frame.getOperandStacks().popIntValue();
                Reference reference = frame.getOperandStacks().popReference();
                if(Reference.NULL.equals(reference)) {
                    throw new NullPointerException();
                }
                ObjectReference objectReference = (ObjectReference) reference;
                objectReference.setIntValue(jvmField.calculateNonStaticFieldOffset(), intValue);
                break;
            case Descriptors.BaseType.FLOAT:
            case Descriptors.BaseType.LONG:
            case Descriptors.BaseType.DOUBLE:
                break;
            default:
                throw new IllegalStateException("Unexpected descriptor: " + descriptor);
        }

        return frame.getJvmThread().getPc() + this.size();
    }

    @Override
    public int size() {
        return 3;
    }

}
