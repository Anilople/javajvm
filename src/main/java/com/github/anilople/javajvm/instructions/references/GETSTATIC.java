package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.constants.Descriptors;
import com.github.anilople.javajvm.heap.JvmField;
import com.github.anilople.javajvm.heap.constant.JvmConstantFieldref;
import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.utils.ByteUtils;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.krb5.internal.crypto.Des;

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
            throw new IncompatibleClassChangeError();
        }

        String descriptor = jvmField.getDescriptor();
        logger.debug("descriptor: {}", descriptor);
        if(Descriptors.isBaseType(descriptor)) {
            // BaseType
            executeGetBaseType(frame, jvmField);
        } else if(Descriptors.isObjectType(descriptor)) {
            // ObjectType
            executeGetObjectType(frame, jvmField);
        } else if(Descriptors.isArrayType(descriptor)){
            // ArrayType
            executeGetArrayType(frame, jvmField);
        } else {
            throw new IllegalStateException("Unexpected descriptor: " + descriptor);
        }

        return frame.getJvmThread().getPc() + this.size();
    }

    public static void executeGetBaseType(Frame frame, JvmField jvmField) {
        logger.trace("base type get: {} {}", jvmField.getDescriptor(), jvmField.getName());
        int staticFieldOffset = jvmField.calculateStaticFieldOffset();
        String descriptor = jvmField.getDescriptor();
        switch (descriptor) {
            case Descriptors.BaseType.BOOLEAN:
                boolean booleanValue = jvmField.getJvmClass().getStaticFieldsValue().getBooleanValue(staticFieldOffset);
                frame.getOperandStacks().pushBooleanValue(booleanValue);
                break;
            case Descriptors.BaseType.BYTE:
                byte byteValue = jvmField.getJvmClass().getStaticFieldsValue().getByteValue(staticFieldOffset);
                frame.getOperandStacks().pushByteValue(byteValue);
                break;
            case Descriptors.BaseType.CHAR:
                char charValue = jvmField.getJvmClass().getStaticFieldsValue().getCharValue(staticFieldOffset);
                frame.getOperandStacks().pushCharValue(charValue);
                break;
            case Descriptors.BaseType.SHORT:
                short shortValue = jvmField.getJvmClass().getStaticFieldsValue().getShortValue(staticFieldOffset);
                frame.getOperandStacks().pushShortValue(shortValue);
                break;
            case Descriptors.BaseType.INT:
                int intValue = jvmField.getJvmClass().getStaticFieldsValue().getIntValue(staticFieldOffset);
                frame.getOperandStacks().pushIntValue(intValue);
                break;
            case Descriptors.BaseType.FLOAT:
                float floatValue = jvmField.getJvmClass().getStaticFieldsValue().getFloatValue(staticFieldOffset);
                frame.getOperandStacks().pushFloatValue(floatValue);
                break;
            case Descriptors.BaseType.LONG:
                long longValue = jvmField.getJvmClass().getStaticFieldsValue().getLongValue(staticFieldOffset);
                frame.getOperandStacks().pushLongValue(longValue);
                break;
            case Descriptors.BaseType.DOUBLE:
                double doubleValue = jvmField.getJvmClass().getStaticFieldsValue().getDoubleValue(staticFieldOffset);
                frame.getOperandStacks().pushDoubleValue(doubleValue);
                break;
            default:
                throw new IllegalStateException("Unexpected descriptor: " + descriptor);
        }
    }

    public static void executeGetObjectType(Frame frame, JvmField jvmField) {
        logger.trace("object type get: {} {}", jvmField.getDescriptor(), jvmField.getName());
        int staticFieldOffset = jvmField.calculateStaticFieldOffset();
        Reference reference = jvmField.getJvmClass().getStaticFieldsValue().getReference(staticFieldOffset);
        frame.getOperandStacks().pushReference(reference);
    }

    /**
     * to do
     * @param frame
     * @param jvmField
     */
    public static void executeGetArrayType(Frame frame, JvmField jvmField) {
        throw new RuntimeException("executeGetArrayType not implement!");
    }

    @Override
    public int size() {
        return 3;
    }

}
