package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.constants.Descriptors;
import com.github.anilople.javajvm.heap.JvmField;
import com.github.anilople.javajvm.heap.constant.JvmConstantFieldref;
import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectReference;
import com.github.anilople.javajvm.utils.ByteUtils;
import com.github.anilople.javajvm.utils.DescriptorUtils;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Operation
 *      Fetch field from object
 *
 * Operand ..., objectref →
 * Stack ..., value
 *
 * Description
 * The objectref, which must be of type reference , is popped from
 * the operand stack.
 *
 * The run-time constant pool item at that index must
 * be a symbolic reference to a field (§5.1), which gives the name and
 * descriptor of the field as well as a symbolic reference to the class
 * in which the field is to be found.
 *
 * The value of the referenced field in objectref is fetched
 * and pushed onto the operand stack.
 *
 * Notes
 * The getfield instruction cannot be used to access the length field
 * of an array. The arraylength instruction (§arraylength) is used
 * instead.
 */
public class GETFIELD implements Instruction {

    private static final Logger logger = LoggerFactory.getLogger(GETFIELD.class);

    private byte indexByte1;

    private byte indexByte2;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.indexByte1 = bytecodeReader.readU1();
        this.indexByte2 = bytecodeReader.readU1();
    }

    @Override
    public int execute(Frame frame) {

        Reference reference = frame.getOperandStacks().popReference();
        if(Reference.NULL.equals(reference)) {
            throw new NullPointerException();
        }
        ObjectReference objectReference = (ObjectReference) reference;

        int index = PrimitiveTypeUtils.intFormUnsignedShort(ByteUtils.bytes2short(indexByte1, indexByte2));
        JvmConstantFieldref jvmConstantFieldref = (JvmConstantFieldref) frame.getJvmMethod().getJvmClass().getJvmConstantPool().getJvmConstant(index);
        JvmField jvmField = jvmConstantFieldref.resolveJvmField();
        logger.debug("jvm field: {}", jvmField);

        // linking exceptions
        if(jvmField.isStatic()) {
            throw new IncompatibleClassChangeError("static field cannot use " + GETFIELD.class);
        }

        String fieldDescriptor = jvmField.getDescriptor();
        logger.debug("field descriptor: {}", fieldDescriptor);
        if(DescriptorUtils.isBaseType(fieldDescriptor)) {
            // BaseType
            executeGetBaseType(frame, jvmField);
        } else if(DescriptorUtils.isObjectType(fieldDescriptor)) {
            // ObjectType
            executeGetObjectType(frame, jvmField);
        } else if(DescriptorUtils.isArrayType(fieldDescriptor)){
            // ArrayType
            executeGetArrayType(frame, jvmField);
        } else {
            throw new IllegalStateException("Unexpected fieldDescriptor: " + fieldDescriptor);
        }

        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
        return frame.getJvmThread().getPc() + this.size();
    }

    private void executeGetBaseType(Frame frame, JvmField jvmField) {
        Reference reference = frame.getOperandStacks().popReference();
        if(Reference.NULL.equals(reference)) {
            throw new NullPointerException();
        }
        ObjectReference objectReference = (ObjectReference) reference;
        String fieldDescriptor = jvmField.getDescriptor();
        switch (fieldDescriptor) {
            case Descriptors.BaseType.BOOLEAN: // boolean
                boolean booleanValue = objectReference.getBooleanValue(jvmField.calculateNonStaticFieldOffset());
                frame.getOperandStacks().pushBooleanValue(booleanValue);
                break;
            case Descriptors.BaseType.BYTE: // byte
                byte byteValue = objectReference.getByteValue(jvmField.calculateNonStaticFieldOffset());
                frame.getOperandStacks().pushByteValue(byteValue);
                break;
            case Descriptors.BaseType.CHAR: // char
                char charValue = objectReference.getCharValue(jvmField.calculateNonStaticFieldOffset());
                frame.getOperandStacks().pushCharValue(charValue);
                break;
            case Descriptors.BaseType.SHORT: // short
                short shortValue = objectReference.getShortValue(jvmField.calculateNonStaticFieldOffset());
                frame.getOperandStacks().pushShortValue(shortValue);
                break;
            case Descriptors.BaseType.INT:
                int value = objectReference.getIntValue(jvmField.calculateNonStaticFieldOffset());
                frame.getOperandStacks().pushIntValue(value);
                break;
            case Descriptors.BaseType.FLOAT:
                float floatValue = objectReference.getFloatValue(jvmField.calculateNonStaticFieldOffset());
                frame.getOperandStacks().pushFloatValue(floatValue);
                break;
            case Descriptors.BaseType.LONG:
                long longValue = objectReference.getLongValue(jvmField.calculateNonStaticFieldOffset());
                frame.getOperandStacks().pushLongValue(longValue);
                break;
            case Descriptors.BaseType.DOUBLE:
                double doubleValue = objectReference.getDoubleValue(jvmField.calculateNonStaticFieldOffset());
                frame.getOperandStacks().pushDoubleValue(doubleValue);
                break;
            default:
                throw new IllegalStateException("Unexpected fieldDescriptor: " + fieldDescriptor);
        }
    }

    private void executeGetObjectType(Frame frame, JvmField jvmField) {
        Reference reference = frame.getOperandStacks().popReference();
        if(Reference.NULL.equals(reference)) {
            throw new NullPointerException();
        }
        ObjectReference objectReference = (ObjectReference) reference;
        int nonStaticFieldOffset = jvmField.calculateNonStaticFieldOffset();
        Reference fieldReference = objectReference.getReference(nonStaticFieldOffset);
        frame.getOperandStacks().pushReference(fieldReference);
    }

    private void executeGetArrayType(Frame frame, JvmField jvmField) {
        Reference reference = frame.getOperandStacks().popReference();
        if(Reference.NULL.equals(reference)) {
            throw new NullPointerException();
        }
        ObjectReference objectReference = (ObjectReference) reference;
        int nonStaticFieldOffset = jvmField.calculateNonStaticFieldOffset();
        Reference fieldReference = objectReference.getReference(nonStaticFieldOffset);
        frame.getOperandStacks().pushReference(fieldReference);
    }

    @Override
    public int size() {
        return 3;
    }

}
