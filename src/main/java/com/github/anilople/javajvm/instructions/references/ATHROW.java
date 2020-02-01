package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectReference;

/**
 * Operation
 * Throw exception or error
 *
 * Operand ..., objectref →
 * Stack objectref
 *
 * Description
 * The objectref must be of type reference and must refer to an
 * object that is an instance of class Throwable or of a subclass of
 * Throwable . It is popped from the operand stack. The objectref is
 * then thrown by searching the current method (§2.6) for the first
 * exception handler that matches the class of objectref, as given by
 * the algorithm in §2.10.
 *
 *
 */
public class ATHROW implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void execute(Frame frame) {
        Reference objectref = frame.getOperandStacks().popReference();
        if(Reference.isNull(objectref)) {
            throw new NullPointerException();
        }
        ObjectReference throwableObjectReference = (ObjectReference) objectref;
        if(!throwableObjectReference.getJvmClass().isSubClassOf(Throwable.class)) {
            throw new RuntimeException(throwableObjectReference + " is not the subclass of " + Throwable.class);
        }

        throw new RuntimeException("Now cannot support " + this.getClass());
//        int nextPc = frame.getNextPc() + this.size();
//        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 1;
    }

}
