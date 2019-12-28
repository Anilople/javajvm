package com.github.anilople.javajvm.instructions.stores;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.BaseTypeArrayReference;
import com.github.anilople.javajvm.runtimedataarea.reference.NullReference;

/**
 * Operation
 * Store into byte or boolean array
 *
 * Operand ..., arrayref, index, value →
 * Stack ...
 *
 * Description
 * The arrayref must be of type reference and must refer to an
 * array whose components are of type byte or of type boolean .
 * The index and the value must both be of type int . The arrayref,
 * index, and value are popped from the operand stack. The int value
 * is truncated to a byte and stored as the component of the array
 * indexed by index.
 *
 * Run-time Exceptions
 * If arrayref is null , bastore throws a NullPointerException .
 * Otherwise, if index is not within the bounds of the array
 * referenced by arrayref, the bastore instruction throws an
 * ArrayIndexOutOfBoundsException
 *
 * Notes
 * The bastore instruction is used to store values into both byte and
 * boolean arrays. In Oracle's Java Virtual Machine implementation,
 * boolean arrays - that is, arrays of type T_BOOLEAN (§2.2,
 * §newarray) - are implemented as arrays of 8-bit values. Other
 * implementations may implement packed boolean arrays; in such
 * implementations the bastore instruction must be able to store
 * boolean values into packed boolean arrays as well as byte values
 * into byte arrays.
 */
public class BASTORE implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void execute(Frame frame) {
        int value = frame.getOperandStacks().popIntValue();
        int index = frame.getOperandStacks().popIntValue();
        Reference reference = frame.getOperandStacks().popReference();
        if(reference instanceof NullReference) {
            throw new NullPointerException();
        }
        BaseTypeArrayReference baseTypeArrayReference = (BaseTypeArrayReference) reference;
        baseTypeArrayReference.assertIndexIsNotOutOfBounds(index);
        if(baseTypeArrayReference.isBooleanType()) {
            // store boolean
            baseTypeArrayReference.setBooleanValue(index, value != 0);
        } else {
            // store byte
            baseTypeArrayReference.setByteValue(index, (byte) value);
        }
        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 1;
    }

}
