package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.heap.JvmMethod;
import com.github.anilople.javajvm.heap.constant.JvmConstantMethodref;
import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.utils.ByteUtils;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Operation
 * Invoke instance method; special handling for superclass, private,
 * and instance initialization method invocations
 *
 * Operand ..., objectref, [arg1, [arg2 ...]] →
 * Stack ...
 *
 * Description
 * The unsigned indexbyte1 and indexbyte2 are used to construct an
 * index into the run-time constant pool of the current class (§2.6),
 * where the value of the index is (indexbyte1 << 8) | indexbyte2.
 *
 * The run-time constant pool item at that index must be a symbolic
 * reference to a method or an interface method (§5.1), which gives
 * the name and descriptor (§4.3.3) of the method as well as a
 * symbolic reference to the class or interface in which the method is
 * to be found. The named method is resolved (§5.4.3.3, §5.4.3.4).
 *
 *
 */
public class INVOKESPECIAL implements Instruction {

    private static final Logger logger = LoggerFactory.getLogger(INVOKESPECIAL.class);

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
        JvmConstantMethodref jvmConstantMethodref = (JvmConstantMethodref) frame.getJvmMethod().getJvmClass().getJvmConstantPool().getJvmConstant(index);
        JvmMethod jvmMethod = jvmConstantMethodref.resolveJvmMethod();
        logger.trace("jvm method: {}", jvmMethod);
        if(jvmMethod.isNative()) {
            logger.debug("native method: {}", jvmMethod);
        }

        // exception
        if(null == jvmMethod) {
            throw new NoSuchMethodError(INVOKESPECIAL.class + " " + jvmMethod);
        }
        if(jvmMethod.isStatic()) {
            throw new IncompatibleClassChangeError(INVOKESPECIAL.class + " " + jvmMethod);
        }

        if(jvmMethod.isAbstract()) {
            throw new AbstractMethodError();
        }


        String descriptor = jvmMethod.getDescriptor();
        logger.trace("descriptor: {}", descriptor);
        // pop args


        // pop object reference
        Reference reference = frame.getOperandStacks().popReference();
        if(Reference.NULL.equals(reference)) {
            throw new NullPointerException(INVOKESPECIAL.class.toString());
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
