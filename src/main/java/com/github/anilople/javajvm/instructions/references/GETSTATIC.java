package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.constants.Descriptors;
import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.heap.JvmField;
import com.github.anilople.javajvm.heap.constant.JvmConstantFieldref;
import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.ArrayReference;
import com.github.anilople.javajvm.utils.ByteUtils;
import com.github.anilople.javajvm.utils.DescriptorUtils;
import com.github.anilople.javajvm.utils.JvmClassUtils;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Operation
 * Get static field from class
 *
 * Operand ..., →
 * Stack ..., value
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
 * The value of the class or interface field is fetched and pushed onto
 * the operand stack.
 */
public class GETSTATIC implements Instruction {

    private static final Logger logger = LoggerFactory.getLogger(GETSTATIC.class);

    private byte indexByte1;

    private byte indexByte2;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.indexByte1 = bytecodeReader.readU1();
        this.indexByte2 = bytecodeReader.readU1();
    }

    @Override
    public void execute(Frame frame) {
        int index = PrimitiveTypeUtils.intFormUnsignedShort(ByteUtils.bytes2short(indexByte1, indexByte2));
        JvmConstantFieldref jvmConstantFieldref = (JvmConstantFieldref) frame.getJvmMethod().getJvmClass().getJvmConstantPool().getJvmConstant(index);
        JvmField jvmField = jvmConstantFieldref.resolveJvmField();
        logger.debug("jvm field: {}", jvmField);

        // linking exceptions
        if(!(jvmField.isStatic() || jvmField.getJvmClass().isInterface())) {
            // if the resolved field is not a static
            // (class) field or an interface field, putstatic throws an
            // IncompatibleClassChangeError
            throw new IncompatibleClassChangeError();
        }

        String descriptor = jvmField.getDescriptor();
        logger.debug("descriptor: {}", descriptor);
        if(DescriptorUtils.isBaseType(descriptor)) {
            // BaseType
            executeGetBaseType(frame, jvmField);
        } else if(DescriptorUtils.isObjectType(descriptor)) {
            // ObjectType
            executeGetObjectType(frame, jvmField);
        } else if(DescriptorUtils.isArrayType(descriptor)){
            // ArrayType
            executeGetArrayType(frame, jvmField);
        } else {
            throw new IllegalStateException("Unexpected descriptor: " + descriptor);
        }

        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    public static void executeGetBaseType(Frame frame, JvmField jvmField) {
        logger.trace("base type get: {} {}", jvmField.getDescriptor(), jvmField.getName());
        JvmClass fieldBelongClass = JvmClassUtils.getJvmClassStaticFieldBelongTo(jvmField.getJvmClass(), jvmField);
        int staticFieldOffset = JvmClassUtils.calculateStaticFieldOffsetInNowClass(fieldBelongClass, jvmField);
        String descriptor = jvmField.getDescriptor();
        switch (descriptor) {
            case Descriptors.BaseType.BOOLEAN:
                boolean booleanValue = fieldBelongClass.getStaticFieldsValue().getBooleanValue(staticFieldOffset);
                frame.getOperandStacks().pushBooleanValue(booleanValue);
                break;
            case Descriptors.BaseType.BYTE:
                byte byteValue = fieldBelongClass.getStaticFieldsValue().getByteValue(staticFieldOffset);
                frame.getOperandStacks().pushByteValue(byteValue);
                break;
            case Descriptors.BaseType.CHAR:
                char charValue = fieldBelongClass.getStaticFieldsValue().getCharValue(staticFieldOffset);
                frame.getOperandStacks().pushCharValue(charValue);
                break;
            case Descriptors.BaseType.SHORT:
                short shortValue = fieldBelongClass.getStaticFieldsValue().getShortValue(staticFieldOffset);
                frame.getOperandStacks().pushShortValue(shortValue);
                break;
            case Descriptors.BaseType.INT:
                int intValue = fieldBelongClass.getStaticFieldsValue().getIntValue(staticFieldOffset);
                frame.getOperandStacks().pushIntValue(intValue);
                break;
            case Descriptors.BaseType.FLOAT:
                float floatValue = fieldBelongClass.getStaticFieldsValue().getFloatValue(staticFieldOffset);
                frame.getOperandStacks().pushFloatValue(floatValue);
                break;
            case Descriptors.BaseType.LONG:
                long longValue = fieldBelongClass.getStaticFieldsValue().getLongValue(staticFieldOffset);
                frame.getOperandStacks().pushLongValue(longValue);
                break;
            case Descriptors.BaseType.DOUBLE:
                double doubleValue = fieldBelongClass.getStaticFieldsValue().getDoubleValue(staticFieldOffset);
                frame.getOperandStacks().pushDoubleValue(doubleValue);
                break;
            default:
                throw new IllegalStateException("Unexpected descriptor: " + descriptor);
        }
    }

    public static void executeGetObjectType(Frame frame, JvmField jvmField) {
        logger.trace("object type get: {} {}", jvmField.getDescriptor(), jvmField.getName());
        JvmClass fieldBelongClass = JvmClassUtils.getJvmClassStaticFieldBelongTo(jvmField.getJvmClass(), jvmField);
        int staticFieldOffset = JvmClassUtils.calculateStaticFieldOffsetInNowClass(fieldBelongClass, jvmField);
        Reference reference = fieldBelongClass.getStaticFieldsValue().getReference(staticFieldOffset);
        frame.getOperandStacks().pushReference(reference);
    }

    /**
     * to do
     * @param frame
     * @param jvmField
     */
    public static void executeGetArrayType(Frame frame, JvmField jvmField) {
        final JvmClass jvmClass = jvmField.getJvmClass();
        final int staticOffset = jvmField.getStaticFieldOffset();
        ArrayReference arrayReference = (ArrayReference) jvmClass.getStaticFieldsValue().getReference(staticOffset);
        frame.getOperandStacks().pushReference(arrayReference);
//        throw new RuntimeException("executeGetArrayType not implement!");
    }

    @Override
    public int size() {
        return 3;
    }

}
