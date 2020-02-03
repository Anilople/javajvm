package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.heap.constant.JvmConstantClass;
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
 * Create new object
 *
 * Operand ... →
 * Stack ..., objectref
 *
 * Description
 * The unsigned indexbyte1 and indexbyte2 are used to construct an
 * index into the run-time constant pool of the current class (§2.6),
 * where the value of the index is (indexbyte1 << 8) | indexbyte2.
 *
 * The run-time constant pool item at the index must be a symbolic
 * reference to a class or interface type.
 *
 * The named class or interface
 * type is resolved (§5.4.3.1) and should result in a class type.
 *
 * Memory for a new instance of that class is allocated from the
 * garbage-collected heap, and the instance variables of the new
 * object are initialized to their default initial values (§2.3, §2.4).
 *
 * The
 * objectref, a reference to the instance, is pushed onto the operand
 * stack.
 */
public class NEW implements Instruction {

    private static final Logger logger = LoggerFactory.getLogger(NEW.class);

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
        JvmConstantClass jvmConstantClass = (JvmConstantClass) frame.getJvmMethod().getJvmClass().getJvmConstantPool().getJvmConstant(index);

        // some type cannot be allocated to an instance
        if(jvmConstantClass.getJvmClass().isInterface()
            || jvmConstantClass.getJvmClass().isAbstract()) {
            throw new InstantiationError("cannot initial " + jvmConstantClass);
        }

        // allocate an object without initial
        JvmClass targetJvmClass = jvmConstantClass.resolveJvmClass();
        logger.debug("try to allocate an object: {}", targetJvmClass);
        ObjectReference objectReference = ObjectReference.makeObjectReference(targetJvmClass);
        frame.getOperandStacks().pushReference(objectReference);

        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 3;
    }

}
