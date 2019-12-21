package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.constants.Descriptors;
import com.github.anilople.javajvm.constants.SpecialMethods;
import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.heap.JvmField;
import com.github.anilople.javajvm.heap.constant.JvmConstantFieldref;
import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.ArrayReference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectReference;
import com.github.anilople.javajvm.utils.ByteUtils;
import com.github.anilople.javajvm.utils.DescriptorUtils;
import com.github.anilople.javajvm.utils.JvmClassUtils;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Operation
 * Set static field in class
 *
 * Operand ..., value →
 * Stack ...
 *
 * Description
 * The unsigned indexbyte1 and indexbyte2 are used to construct an
 * index into the run-time constant pool of the current class (§2.6),
 * where the value of the index is (indexbyte1 << 8) | indexbyte2.
 *
 * The run-time constant pool item at that index must be a symbolic
 * reference to a field (§5.1), which gives the name and descriptor of
 * the field as well as a symbolic reference to the class or interface
 * in which the field is to be found. The referenced field is resolved
 * (§5.4.3.2).
 *
 * On successful resolution of the field, the class or interface that
 * declared the resolved field is initialized (§5.5) if that class or
 * interface has not already been initialized.
 *
 * The type of a value stored by a putstatic instruction must be
 * compatible with the descriptor of the referenced field (§4.3.2).
 *
 * If
 * the field descriptor type is boolean , byte , char , short , or int ,
 * then the value must be an int .
 * If the field descriptor type is float ,
 * long , or double , then the value must be a float , long , or double ,
 * respectively.
 *
 * If the field descriptor type is a reference type, then
 * the value must be of a type that is assignment compatible (JLS
 * §5.2) with the field descriptor type.
 *
 * If the field is final, it must be
 * declared in the current class, and the instruction must occur in the
 * <clinit> method of the current class (§2.9).
 *
 *
 */
public class PUTSTATIC implements Instruction {

    private static final Logger logger = LoggerFactory.getLogger(PUTSTATIC.class);

    private byte indexByte1;

    private byte indexByte2;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.indexByte1 = bytecodeReader.readU1();
        this.indexByte2 = bytecodeReader.readU1();
    }

    @Override
    public int execute(Frame frame) {
        int index = PrimitiveTypeUtils.intFormUnsignedShort(ByteUtils.bytes2short(indexByte1, indexByte2));
        JvmConstantFieldref jvmConstantFieldref = (JvmConstantFieldref) frame.getJvmMethod().getJvmClass().getJvmConstantPool().getJvmConstant(index);
        JvmField jvmField = jvmConstantFieldref.resolveJvmField();
        logger.debug("jvm field: {}", jvmField);

        // linking exceptions
        if(!(jvmField.isStatic() || jvmField.getJvmClass().isInterface())) {
            // if the resolved field is not a static
            // (class) field or an interface field, putstatic throws an
            // IncompatibleClassChangeError
            logger.error("IncompatibleClassChangeError");
            throw new IncompatibleClassChangeError();
        }
        if(jvmField.isFinal()) {
            // if the field is final , it must be declared in the current
            // class, and the instruction must occur in the <clinit> method of
            // the current class. Otherwise, an IllegalAccessError is thrown.

            // check belong to current class or not, to do...

            // check this method is <clinit> or not
            if(!SpecialMethods.CLINIT.equals(frame.getJvmMethod().getName())) {
                throw new IllegalAccessError("the instruction must occur in the <clinit> method");
            }
        }

        String descriptor = jvmField.getDescriptor();
        logger.debug("descriptor: {}", descriptor);
        if(DescriptorUtils.isBaseType(descriptor)) {
            // BaseType
            executePutBaseType(frame, jvmField);
        } else if(DescriptorUtils.isObjectType(descriptor)) {
            // ObjectType
            executePutObjectType(frame, jvmField);
        } else if(DescriptorUtils.isArrayType(descriptor)){
            // ArrayType
            executePutArrayType(frame, jvmField);
        } else {
            throw new IllegalStateException("Unexpected descriptor: " + descriptor);
        }

        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
        return frame.getJvmThread().getPc() + this.size();
    }

    public static void executePutBaseType(Frame frame, JvmField jvmField) {
        JvmClass fieldBelongClass = JvmClassUtils.getJvmClassStaticFieldBelongTo(jvmField.getJvmClass(), jvmField);
        int staticFieldOffset = JvmClassUtils.calculateStaticFieldOffsetInNowClass(fieldBelongClass, jvmField);
        String descriptor = jvmField.getDescriptor();
        switch (descriptor) {
            case Descriptors.BaseType.BOOLEAN: // boolean
                boolean booleanValue = frame.getOperandStacks().popBooleanValue();
                fieldBelongClass.getStaticFieldsValue().setBooleanValue(staticFieldOffset, booleanValue);
                break;
            case Descriptors.BaseType.BYTE: // byte
                byte byteValue = frame.getOperandStacks().popByteValue();
                fieldBelongClass.getStaticFieldsValue().setByteValue(staticFieldOffset, byteValue);
                break;
            case Descriptors.BaseType.CHAR: // char
                char charValue = frame.getOperandStacks().popCharValue();
                fieldBelongClass.getStaticFieldsValue().setCharValue(staticFieldOffset, charValue);
                break;
            case Descriptors.BaseType.SHORT: // short
                short shortValue = frame.getOperandStacks().popShortValue();
                fieldBelongClass.getStaticFieldsValue().setShortValue(staticFieldOffset, shortValue);
                break;
            case Descriptors.BaseType.INT:
                int intValue = frame.getOperandStacks().popIntValue();
                fieldBelongClass.getStaticFieldsValue().setIntValue(staticFieldOffset, intValue);
                break;
            case Descriptors.BaseType.FLOAT:
                float floatValue = frame.getOperandStacks().popFloatValue();
                fieldBelongClass.getStaticFieldsValue().setFloatValue(staticFieldOffset, floatValue);
                break;
            case Descriptors.BaseType.LONG:
                long longValue = frame.getOperandStacks().popLongValue();
                fieldBelongClass.getStaticFieldsValue().setLongValue(staticFieldOffset, longValue);
                break;
            case Descriptors.BaseType.DOUBLE:
                double doubleValue = frame.getOperandStacks().popDoubleValue();
                fieldBelongClass.getStaticFieldsValue().setDoubleValue(staticFieldOffset, doubleValue);
                break;
            default:
                throw new IllegalStateException("Unexpected descriptor: " + descriptor);
        }
    }

    public static void executePutObjectType(Frame frame, JvmField jvmField) {
        JvmClass fieldBelongClass = JvmClassUtils.getJvmClassStaticFieldBelongTo(jvmField.getJvmClass(), jvmField);
        int staticFieldOffset = JvmClassUtils.calculateStaticFieldOffsetInNowClass(fieldBelongClass, jvmField);
        Reference reference = frame.getOperandStacks().popReference();
        fieldBelongClass.getStaticFieldsValue().setReference(staticFieldOffset, reference);
    }

    public static void executePutArrayType(Frame frame, JvmField jvmField) {
        JvmClass fieldBelongClass = JvmClassUtils.getJvmClassStaticFieldBelongTo(jvmField.getJvmClass(), jvmField);
        int staticFieldOffset = JvmClassUtils.calculateStaticFieldOffsetInNowClass(fieldBelongClass, jvmField);
        ArrayReference arrayReference = (ArrayReference) frame.getOperandStacks().popReference();
        fieldBelongClass.getStaticFieldsValue().setReference(staticFieldOffset, arrayReference);
    }

    @Override
    public int size() {
        return 3;
    }

}
