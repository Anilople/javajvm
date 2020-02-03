package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.constants.ArrayTypeCodes;
import com.github.anilople.javajvm.heap.JvmClassLoader;
import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.reference.BaseTypeArrayReference;


/**
 * Operation
 * Create new array
 *
 * Operand ..., count →
 * Stack ..., arrayref
 *
 * Description
 * The count must be of type int . It is popped off the operand stack.
 * The count represents the number of elements in the array to be
 * created.
 *
 * The atype is a code that indicates the type of array to create. It must
 * take one of the following values:
 *
 * Description
 * The count must be of type int . It is popped off the operand stack.
 * The count represents the number of elements in the array to be
 * created.
 * The atype is a code that indicates the type of array to create. It must
 * take one of the following values:
 *
 * @see com.github.anilople.javajvm.constants.ArrayTypeCodes
 *
 * A new array whose components are of type atype and of length
 * count is allocated from the garbage-collected heap. A reference
 * arrayref to this new array object is pushed into the operand stack.
 * Each of the elements of the new array is initialized to the default
 * initial value (§2.3, §2.4) for the element type of the array type.
 */
public class NEWARRAY implements Instruction {

    private byte atype;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.atype = bytecodeReader.readU1();
    }

    @Override
    public void execute(Frame frame) {
        int count = frame.getOperandStacks().popIntValue();
        // Run-time Exception
        // If count is less than zero, newarray throws a
        // NegativeArraySizeException
        if(count < 0) {
            throw new NegativeArraySizeException(count + " is less than 0");
        }

        final JvmClassLoader jvmClassLoader = frame.getJvmMethod().getJvmClass().getLoader();

        frame.getOperandStacks().pushReference(
                NEWARRAY.allocate(jvmClassLoader, atype, count)
        );

        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 2;
    }

    /**
     * allocate base type array
     * 1 dimensions
     * @param jvmClassLoader class loader
     * @param typeCode type
     * @see ArrayTypeCodes
     * @param count array's length
     * @return
     */
    public static BaseTypeArrayReference allocate(JvmClassLoader jvmClassLoader, byte typeCode, int count) {
        return new BaseTypeArrayReference(jvmClassLoader, typeCode, count);
    }

    /**
     * allocate base type array
     * 1 dimensions
     * @param jvmClassLoader class loader
     * @param typeDescriptor descriptor of base type
     * @see com.github.anilople.javajvm.constants.Descriptors.BaseType
     * @param count
     * @return
     */
    public static BaseTypeArrayReference allocate(JvmClassLoader jvmClassLoader, String typeDescriptor, int count) {
        byte typeCode = ArrayTypeCodes.fromDescriptor(typeDescriptor);
        return allocate(jvmClassLoader, typeCode, count);
    }
}
