package com.github.anilople.javajvm.instructions.loads;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.BaseTypeArrayReference;
import com.github.anilople.javajvm.runtimedataarea.reference.NullReference;

/**
 * Operation
 * Load byte or boolean from array
 *
 * Operand ..., arrayref, index →
 * Stack ..., value
 *
 * Description
 * The arrayref must be of type reference and must refer to an array
 * whose components are of type byte or of type boolean . The index
 * must be of type int . Both arrayref and index are popped from the
 * operand stack. The byte value in the component of the array at
 * index is retrieved, sign-extended to an int value, and pushed onto
 * the top of the operand stack.
 *
 * Run-time Exceptions
 * If arrayref is null , baload throws a NullPointerException .
 * Otherwise, if index is not within the bounds of the array
 * referenced by arrayref, the baload instruction throws an
 * ArrayIndexOutOfBoundsException
 *
 * Notes
 * The baload instruction is used to load values from both byte and
 * boolean arrays. In Oracle's Java Virtual Machine implementation,
 * boolean arrays - that is, arrays of type T_BOOLEAN (§2.2,
 * §newarray) - are implemented as arrays of 8-bit values. Other
 * implementations may implement packed boolean arrays; the
 * baload instruction of such implementations must be used to access
 * those arrays
 */
public class BALOAD implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void execute(Frame frame) {
        int index = frame.getOperandStacks().popIntValue();
        Reference reference = frame.getOperandStacks().popReference();
        if(reference instanceof NullReference) {
            throw new NullPointerException();
        }
        BaseTypeArrayReference baseTypeArrayReference = (BaseTypeArrayReference) reference;
        baseTypeArrayReference.assertIndexIsNotOutOfBounds(index);
        if(baseTypeArrayReference.isBooleanType()) {
            // load boolean
            boolean booleanValue = baseTypeArrayReference.getBooleanValue(index);
            frame.getOperandStacks().pushBooleanValue(booleanValue);
        } else {
            // load byte
            // byte sign-extended to an int value
            byte byteValue = baseTypeArrayReference.getByteValue(index);
            frame.getOperandStacks().pushByteValue(byteValue);
        }

        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 1;
    }

}
