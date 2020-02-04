package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.heap.constant.JvmConstant;
import com.github.anilople.javajvm.heap.constant.JvmConstantClass;
import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.ArrayReference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectReference;
import com.github.anilople.javajvm.utils.ByteUtils;
import com.github.anilople.javajvm.utils.JvmClassUtils;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;

/**
 * Operation Check whether object is of given type
 *
 * Operand ..., objectref →
 * Stack ..., objectref
 *
 * Description
 * The objectref must be of type reference . The unsigned
 * indexbyte1 and indexbyte2 are used to construct an index into
 * the run-time constant pool of the current class (§2.6), where the
 * value of the index is (indexbyte1 << 8) | indexbyte2. The run-time
 * constant pool item at the index must be a symbolic reference to a
 * class, array, or interface type.
 *
 * If objectref is null , then the operand stack is unchanged.
 *
 * Otherwise, the named class, array, or interface type is resolved
 * (§5.4.3.1). If objectref can be cast to the resolved class, array,
 * or interface type, the operand stack is unchanged; otherwise, the
 * checkcast instruction throws a ClassCastException .
 * The following rules are used to determine whether an objectref
 * that is not null can be cast to the resolved type: if S is the class of
 * the object referred to by objectref and T is the resolved class, array,
 * or interface type, checkcast determines whether objectref can be
 * cast to type T as follows:
 */
public class CHECKCAST implements Instruction {

    private byte indexByte1;

    private byte indexByte2;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.indexByte1 = bytecodeReader.readU1();
        this.indexByte2 = bytecodeReader.readU1();
    }

    @Override
    public void execute(Frame frame) {
        Reference reference = frame.getOperandStacks().popReference();
        if(Reference.isNull(reference)) {
            // If objectref is null , then the operand stack is unchanged.
            frame.getOperandStacks().pushReference(reference);
        } else if(Reference.isArray(reference)) {
            checkArrayReference(frame, (ArrayReference) reference);
        } else {
            checkObjectReference(frame, (ObjectReference) reference);
        }
        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    /**
     * the reference in operand stack is an ArrayReference
     * @param frame
     * @param arrayReference
     */
    private void checkArrayReference(Frame frame, ArrayReference arrayReference) {
        JvmClass S = arrayReference.resolveType();
        JvmClass T = this.resolveJvmClassT(frame);
        checkCast(frame, arrayReference, S, T);
    }

    /**
     * the reference in operand stack is an ObjectReference
     * @param frame
     * @param objectReference
     */
    private void checkObjectReference(Frame frame, ObjectReference objectReference) {
        // S is the class of the object referred to by objectref
        JvmClass S = objectReference.getJvmClass();
        JvmClass T = this.resolveJvmClassT(frame);
        checkCast(frame, objectReference, S, T);
    }

    /**
     * check cast of S and T,
     * will change operand stack
     * @param frame
     * @param reference
     * @param S
     * @param T
     */
    private static void checkCast(Frame frame, Reference reference, JvmClass S, JvmClass T) {
        if(JvmClassUtils.typeCast(S, T)) {
            // push the reference
            frame.getOperandStacks().pushReference(reference);
        } else {
            throw new ClassCastException(S.getName() + " cannot cast to " + T.getName());
        }
    }

    @Override
    public int size() {
        return 3;
    }

    public int resolveIndex() {
        short indexShort = ByteUtils.bytes2short(indexByte1, indexByte2);
        return PrimitiveTypeUtils.intFormUnsignedShort(indexShort);
    }

    /**
     * The run-time
     * constant pool item at the index must be a symbolic reference to a
     * class, array, or interface type.
     * @param frame
     * @return
     */
    public JvmClass resolveJvmClassT(Frame frame) {
        int index = resolveIndex();
        JvmConstant jvmConstant = frame.getJvmMethod().getJvmClass().getJvmConstantPool().getJvmConstant(index);
        JvmConstantClass jvmConstantClass = (JvmConstantClass) jvmConstant;
        return jvmConstantClass.resolveJvmClass();
    }
}
