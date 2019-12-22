package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.heap.constant.JvmConstant;
import com.github.anilople.javajvm.heap.constant.JvmConstantClass;
import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectArrayReference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectReference;
import com.github.anilople.javajvm.utils.ByteUtils;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Operation
 * Create new array of reference
 *
 * Operand ..., count →
 * Stack ..., arrayref
 *
 * Description
 * The count must be of type int . It is popped off the operand stack.
 * The count represents the number of components of the array to
 * be created.
 *
 * The unsigned indexbyte1 and indexbyte2 are used to
 * construct an index into the run-time constant pool of the current
 * class (§2.6), where the value of the index is (indexbyte1 << 8) |
 * indexbyte2.
 *
 * The run-time constant pool item at that index must
 * be a symbolic reference to a class, array, or interface type.
 *
 * The
 * named class, array, or interface type is resolved (§5.4.3.1). A new
 * array with components of that type, of length count, is allocated
 * from the garbage-collected heap, and a reference arrayref to this
 * new array object is pushed onto the operand stack.
 *
 * All components
 * of the new array are initialized to null , the default value for
 * reference types (§2.4).
 */
public class ANEWARRAY implements Instruction {

    private static final Logger logger = LoggerFactory.getLogger(ANEWARRAY.class);

    private byte indexbyte1;

    private byte indexbyte2;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.indexbyte1 = bytecodeReader.readU1();
        this.indexbyte2 = bytecodeReader.readU1();
    }

    @Override
    public void execute(Frame frame) {
        int count = frame.getOperandStacks().popIntValue();
        // Run-time Exceptions
        // Otherwise, if count is less than zero, the anewarray instruction
        // throws a NegativeArraySizeException
        if(count < 0) {
            throw new NegativeArraySizeException("" + count);
        }


        int index = PrimitiveTypeUtils.intFormSignedShort(ByteUtils.bytes2short(indexbyte1, indexbyte2));
        logger.trace("index = {}", index);
        // class, array, or interface type
        JvmConstant jvmConstant = frame.getJvmMethod().getJvmClass().getJvmConstantPool().getJvmConstant(index);
        logger.trace("jvmConstant = {}", jvmConstant);
        if(jvmConstant instanceof JvmConstantClass) {
            JvmConstantClass jvmConstantClass = (JvmConstantClass) jvmConstant;
            logger.trace("className = {}", jvmConstantClass.getName());
            ObjectReference objectReference = new ObjectReference(jvmConstantClass.getJvmClass());
            ObjectArrayReference objectArrayReference = new ObjectArrayReference(objectReference, count);
            frame.getOperandStacks().pushReference(objectArrayReference);
        } else {
            throw new IllegalStateException();
        }


        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 3;
    }

}
