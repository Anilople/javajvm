package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.heap.constant.JvmConstant;
import com.github.anilople.javajvm.heap.constant.JvmConstantClass;
import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectReference;
import com.github.anilople.javajvm.utils.ByteUtils;
import com.github.anilople.javajvm.utils.JvmClassUtils;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;

/**
 * Operation
 * Determine if object is of given type
 *
 * Operand ..., objectref →
 * Stack ..., result
 *
 * Description
 * The objectref, which must be of type reference , is popped from
 * the operand stack. The unsigned indexbyte1 and indexbyte2 are
 * used to construct an index into the run-time constant pool of the
 * current class (§2.6), where the value of the index is (indexbyte1 <<
 * 8) | indexbyte2. The run-time constant pool item at the index must
 * be a symbolic reference to a class, array, or interface type.
 *
 *
 */
public class INSTANCEOF implements Instruction {

    private byte indexByte1;

    private byte indexByte2;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.indexByte1 = bytecodeReader.readU1();
        this.indexByte2 = bytecodeReader.readU1();
    }

    @Override
    public void execute(Frame frame) {
        final JvmClass currentClass = frame.getJvmMethod().getJvmClass();
        final int index = this.resolveIndex();
        final ObjectReference objectref = (ObjectReference) frame.getOperandStacks().popReference();
        if(Reference.isNull(objectref)) {
            frame.getOperandStacks().pushIntValue(0);
        } else {
            JvmConstant jvmConstant = currentClass.getJvmConstantPool().getJvmConstant(index);
            // a symbolic reference to a class, array, or interface type
            JvmConstantClass jvmConstantClass = (JvmConstantClass) jvmConstant;
            JvmClass T = jvmConstantClass.resolveJvmClass();
            JvmClass S = objectref.getJvmClass();
            if(JvmClassUtils.typeCast(S, T)) {
                frame.getOperandStacks().pushIntValue(1);
            } else {
                frame.getOperandStacks().pushIntValue(0);
            }
        }
        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 3;
    }

    /**
     * The unsigned indexbyte1 and indexbyte2 are
     * used to construct an index into the run-time constant pool of the
     * current class (§2.6), where the value of the index is (indexbyte1 <<
     * 8) | indexbyte2.
     * @return
     */
    public int resolveIndex() {
        short shortIndex = ByteUtils.bytes2short(indexByte1, indexByte2);
        return PrimitiveTypeUtils.intFormUnsignedShort(shortIndex);
    }
}
