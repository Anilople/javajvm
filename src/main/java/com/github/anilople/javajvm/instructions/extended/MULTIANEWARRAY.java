package com.github.anilople.javajvm.instructions.extended;

import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.heap.constant.JvmConstant;
import com.github.anilople.javajvm.heap.constant.JvmConstantClass;
import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.instructions.references.ANEWARRAY;
import com.github.anilople.javajvm.instructions.references.NEWARRAY;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.reference.ArrayReference;
import com.github.anilople.javajvm.runtimedataarea.reference.BaseTypeArrayReference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectArrayReference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectReference;
import com.github.anilople.javajvm.utils.ByteUtils;
import com.github.anilople.javajvm.utils.DescriptorUtils;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Operation
 * Create new multidimensional array
 *
 * Operand ..., count1, [count2, ...] →
 * Stack ..., arrayref
 *
 * Description
 * The dimensions operand is an unsigned byte that must be greater
 * than or equal to 1. It represents the number of dimensions of the
 * array to be created. The operand stack must contain dimensions
 * values. Each such value represents the number of components in
 * a dimension of the array to be created, must be of type int , and
 * must be non-negative. The count1 is the desired length in the first
 * dimension, count2 in the second, etc.
 *
 * All of the count values are popped off the operand stack. The
 * unsigned indexbyte1 and indexbyte2 are used to construct an index
 * into the run-time constant pool of the current class (§2.6), where
 * the value of the index is (indexbyte1 << 8) | indexbyte2. The run-
 * time constant pool item at the index must be a symbolic reference
 * to a class, array, or interface type. The named class, array, or
 * interface type is resolved (§5.4.3.1). The resulting entry must be
 * an array class type of dimensionality greater than or equal to
 * dimensions.
 *
 * A new multidimensional array of the array type is allocated
 * from the garbage-collected heap. If any count value is zero, no
 * subsequent dimensions are allocated.
 * The components of the array
 * in the first dimension are initialized to subarrays of the type of the
 * second dimension, and so on. The components of the last allocated
 * dimension of the array are initialized to the default initial value
 * (§2.3, §2.4) for the element type of the array type. A reference
 * arrayref to the new array is pushed onto the operand stack.
 *
 * Linking Exceptions
 * During resolution of the symbolic reference to the class, array, or
 * interface type, any of the exceptions documented in §5.4.3.1 can
 * be thrown.
 * Otherwise, if the current class does not have permission to access
 * the element type of the resolved array class, multianewarray
 * throws an IllegalAccessError
 *
 * Run-time
 * Exception
 * Otherwise, if any of the dimensions values on the operand
 * stack are less than zero, the multianewarray instruction throws a
 * NegativeArraySizeException
 *
 * Notes
 * It may be more efficient to use newarray or anewarray
 * (§newarray, §anewarray) when creating an array of a single
 * dimension.
 * The array class referenced via the run-time constant pool may
 * have more dimensions than the dimensions operand of the
 * multianewarray instruction. In that case, only the first dimensions
 * of the dimensions of the array are created.
 */
public class MULTIANEWARRAY implements Instruction {

    private static final Logger logger = LoggerFactory.getLogger(MULTIANEWARRAY.class);

    private byte indexByte1;

    private byte indexByte2;

    private byte dimensions;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.indexByte1 = bytecodeReader.readU1();
        this.indexByte2 = bytecodeReader.readU1();
        this.dimensions = bytecodeReader.readU1();
    }

    @Override
    public void execute(Frame frame) {
        int index = resolveIndex();

        // must be a symbolic reference
        // to a class, array, or interface type.
        JvmConstant jvmConstant = frame.getJvmMethod().getJvmClass().getJvmConstantPool().getJvmConstant(index);
        JvmConstantClass jvmConstantClass = (JvmConstantClass) jvmConstant;
        logger.debug("{} will make {}", this.getClass(), jvmConstantClass.getName());

        int dimensions = PrimitiveTypeUtils.intFormUnsignedByte(this.dimensions);
        // get every dimensions
        List<Integer> counts = popCounts(frame, dimensions);
        // check
        checkCounts(counts);

        // allocate it
        ArrayReference arrayReference = MULTIANEWARRAY.allocate(jvmConstantClass.resolveJvmClass(), counts);
        // push it to the top of operand stack
        frame.getOperandStacks().pushReference(arrayReference);

        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 4;
    }

    public int resolveIndex() {
        short indexShort = ByteUtils.bytes2short(indexByte1, indexByte2);
        return PrimitiveTypeUtils.intFormUnsignedShort(indexShort);
    }

    /**
     * pop count1, [count2, ...] → from operand stack
     * @param frame
     * @param dimensions the number of count
     * @return list of [count1, count2, ...]
     */
    private static List<Integer> popCounts(Frame frame, int dimensions) {
        List<Integer> integers = new ArrayList<>();
        for(int i = 0; i < dimensions; i++) {
            int count = frame.getOperandStacks().popIntValue();
            integers.add(count);
        }
        // remember reverse them
        Collections.reverse(integers);
        return integers;
    }

    /**
     * Run-time Exception
     *
     * Otherwise, if any of the dimensions values on the operand
     * stack are less than zero, the multianewarray instruction throws a
     * NegativeArraySizeException
     *
     * @param counts
     * @throws NegativeArraySizeException
     */
    private static void checkCounts(List<Integer> counts) {
        for(Integer count : counts) {
            if(count < 0) {
                throw new NegativeArraySizeException(counts.toString());
            }
        }
    }

    /**
     * allocate multiple array
     * @param jvmClass now the multiple array's class
     * @param counts every dimensions
     * @return
     */
    public static ArrayReference allocate(JvmClass jvmClass, List<Integer> counts) {
        if(0 == counts.size()) {
            throw new RuntimeException("you cannot pass 0 size dimensions" + counts);
        } else if(1 == counts.size()) {
            if(DescriptorUtils.getDimensions(jvmClass.getName()) > 1) {
                // through that
                // The resulting entry must be
                // an array class type of dimensionality greater than or equal to
                // dimensions.

                // to do
                throw new RuntimeException("dimension in descriptor must same as counts size" + jvmClass.getName());
            }

            int count = counts.get(0);

            String subTypeDescriptor = jvmClass.getName().substring(1);
            if(DescriptorUtils.isBaseType(subTypeDescriptor)) {
                // last dimension is base type array
                return NEWARRAY.allocate(subTypeDescriptor, count);
            } else {
                // last dimension is object type array
                // the descriptor like "Ljava/lang/String;", so we need to delete 'L' in left, ';' in right
                String className = DescriptorUtils.getClassName(subTypeDescriptor);
                JvmClass objectClass = jvmClass.getLoader().loadClass(className);
                return ANEWARRAY.allocate(objectClass, count);
            }

        } else {
            // multiple
            int headCount = counts.get(0);
            ObjectArrayReference objectArrayReference = new ObjectArrayReference(new ObjectReference(jvmClass), headCount);
            JvmClass subDimensionsClass = jvmClass.getLoader().loadClass(jvmClass.getName().substring(1));
            // use recursion to allocate the rest of this multiple array
            for(int i = 0; i < headCount; i++) {
                objectArrayReference.setReference(
                        i,
                        allocate(subDimensionsClass, counts.subList(1, counts.size()))
                );
            }
            return objectArrayReference;
        }
    }

}
