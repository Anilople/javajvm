package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.constants.Descriptors;
import com.github.anilople.javajvm.constants.SpecialMethods;
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
        if(jvmField.isFinal() && !SpecialMethods.CLINIT.equals(jvmField.getName())) {
            // if the field is final , it must be declared in the current
            // class, and the instruction must occur in the <clinit> method of
            // the current class. Otherwise, an IllegalAccessError is thrown.
            logger.error("IllegalAccessError");
            throw new IllegalAccessError();
        }

        String descriptor = jvmField.getDescriptor();
        logger.debug("descriptor: {}", descriptor);
        int staticFieldOffset = jvmField.calculateStaticFieldOffset();
        switch (descriptor) {
            case Descriptors.BaseType.BOOLEAN: // boolean
                boolean booleanValue = frame.getOperandStacks().popBooleanValue();
                jvmField.getJvmClass().getStaticFieldsValue().setBooleanValue(index, booleanValue);
                break;
            case Descriptors.BaseType.BYTE: // byte
                byte byteValue = frame.getOperandStacks().popByteValue();
                jvmField.getJvmClass().getStaticFieldsValue().setByteValue(index, byteValue);
                break;
            case Descriptors.BaseType.CHAR: // char
                char charValue = frame.getOperandStacks().popCharValue();
                jvmField.getJvmClass().getStaticFieldsValue().setCharValue(index, charValue);
                break;
            case Descriptors.BaseType.SHORT: // short
                short shortValue = frame.getOperandStacks().popShortValue();
                jvmField.getJvmClass().getStaticFieldsValue().setShortValue(index, shortValue);
                break;
            case Descriptors.BaseType.INT:
                int intValue = frame.getOperandStacks().popIntValue();
                jvmField.getJvmClass().getStaticFieldsValue().setIntValue(staticFieldOffset, intValue);
                break;
            case Descriptors.BaseType.FLOAT:
                float floatValue = frame.getOperandStacks().popFloatValue();
                jvmField.getJvmClass().getStaticFieldsValue().setFloatValue(index, floatValue);
                break;
            case Descriptors.BaseType.LONG:
                long longValue = frame.getOperandStacks().popLongValue();
                jvmField.getJvmClass().getStaticFieldsValue().setLongValue(index, longValue);
                break;
            case Descriptors.BaseType.DOUBLE:
                double doubleValue = frame.getOperandStacks().popDoubleValue();
                jvmField.getJvmClass().getStaticFieldsValue().setDoubleValue(index, doubleValue);
                break;
            default:
                throw new IllegalStateException("Unexpected descriptor: " + descriptor);
        }
        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
        return frame.getJvmThread().getPc() + this.size();
    }

    @Override
    public int size() {
        return 3;
    }

}
