package com.github.anilople.javajvm.instructions.stores;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.ArrayReference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectArrayReference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Operation
 * Store into reference array
 *
 * Operand ..., arrayref, index, value →
 * Stack ...
 *
 * Description
 * The arrayref must be of type reference and must refer to
 * an array whose components are of type reference . The index
 * must be of type int and value must be of type reference . The
 * arrayref, index, and value are popped from the operand stack. The
 * reference value is stored as the component of the array at index.
 *
 * type check follow:
 *
 * At run time, the type of value must be compatible with the type of
 * the components of the array referenced by arrayref. Specifically,
 * assignment of a value of reference type S (source) to an array
 * component of reference type T (target) is allowed only if:
 *
 *
 */
public class AASTORE implements Instruction {

    private static final Logger logger = LoggerFactory.getLogger(AASTORE.class);

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void execute(Frame frame) {
        Reference value = frame.getOperandStacks().popReference();
        int index = frame.getOperandStacks().popIntValue();

        // arrayref
        Reference reference = frame.getOperandStacks().popReference();
        Reference.assertIsNotNull(reference);
        ObjectArrayReference objectArrayReference = (ObjectArrayReference) reference;
        objectArrayReference.assertIndexIsNotOutOfBounds(index);

        // type check here, jvms8: Page 368
        // null do not need to type check
        logger.trace("value, i.e source = {}, array component of reference type target = {}", value, objectArrayReference.getComponentTypeReference());
        if(!Reference.NULL.equals(value)) {
            // to do

            // S i.e value
            if(value instanceof ObjectReference) {
                // S is class or interface type
            } else if(value instanceof ArrayReference) {
                // S is an array type
            } else {
                throw new IllegalStateException("not match type: " + value);
            }
        }

        // store to array here
        objectArrayReference.setReference(index, value);

        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 1;
    }

}
