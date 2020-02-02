package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.heap.JvmConstantPool;
import com.github.anilople.javajvm.heap.JvmMethod;
import com.github.anilople.javajvm.heap.constant.JvmConstant;
import com.github.anilople.javajvm.heap.constant.JvmConstantClass;
import com.github.anilople.javajvm.heap.constant.JvmConstantInterfaceMethodref;
import com.github.anilople.javajvm.heap.constant.JvmConstantNameAndType;
import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.LocalVariables;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectReference;
import com.github.anilople.javajvm.utils.ByteUtils;
import com.github.anilople.javajvm.utils.DescriptorUtils;
import com.github.anilople.javajvm.utils.HackUtils;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Operation
 * Invoke interface method
 *
 * Operand ..., objectref, [arg1, [arg2 ...]] →
 * Stack ...
 *
 * Description
 * The unsigned indexbyte1 and indexbyte2 are used to construct an
 * index into the run-time constant pool of the current class (§2.6),
 * where the value of the index is (indexbyte1 << 8) | indexbyte2.
 * The run-time constant pool item at that index must be a symbolic
 * reference to an interface method (§5.1), which gives the name and
 * descriptor (§4.3.3) of the interface method as well as a symbolic
 * reference to the interface in which the interface method is to be
 * found. The named interface method is resolved (§5.4.3.4).
 *
 */
public class INVOKEINTERFACE implements Instruction {

    private static final Logger logger = LoggerFactory.getLogger(INVOKEINTERFACE.class);

    private byte indexByte1;

    private byte indexByte2;

    /**
     * The count operand is an unsigned byte that must not be zero
     */
    private byte count;

    /**
     * The value of the fourth operand byte
     * must always be zero.
     */
    private byte zero;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.indexByte1 = bytecodeReader.readU1();
        this.indexByte2 = bytecodeReader.readU1();
        this.count = bytecodeReader.readU1();
        this.zero = bytecodeReader.readU1();
    }

    @Override
    public void execute(Frame frame) {
        final JvmMethod currentMethod = frame.getJvmMethod();
        final JvmClass currentClass = currentMethod.getJvmClass();
        final JvmConstantPool currentConstantPool = currentClass.getJvmConstantPool();

        // jvm specification. 5.4.3.4 Interface Method Resolution
        int index = this.resolveIndex();
        JvmConstantInterfaceMethodref jvmConstantInterfaceMethodref = (JvmConstantInterfaceMethodref) currentConstantPool.getJvmConstant(index);

        final JvmConstantClass jvmConstantClass = jvmConstantInterfaceMethodref.resolveJvmConstantClass();
        final JvmConstantNameAndType jvmConstantNameAndType = jvmConstantInterfaceMethodref.resolveJvmConstantNameAndType();
        final JvmClass interfaceClass = jvmConstantClass.resolveJvmClass();
        if(!interfaceClass.isInterface()) {
            throw new IncompatibleClassChangeError(interfaceClass + " is not an interface");
        }
        final String methodName = jvmConstantNameAndType.getName();
        final String methodDescriptor = jvmConstantNameAndType.getDescriptor();

        logger.trace("method descriptor: {}", methodDescriptor);
        List<String> parameterDescriptors = DescriptorUtils.getParameterDescriptor(methodDescriptor);
        // pop args and object reference
        LocalVariables localVariables = DescriptorUtils.popArgsByParameterDescriptor(
                true,
                frame.getOperandStacks(),
                parameterDescriptors
        );

        final Reference reference = localVariables.getReference(0);
        if(Reference.isNull(reference)) {
            throw new NullPointerException();
        }
        final ObjectReference objectref = (ObjectReference) reference;
        final JvmMethod jvmMethod = objectref.getJvmClass().getMethod(methodName, methodDescriptor);
        if(jvmMethod.isNative()) {
            throw new RuntimeException(this.getClass() + " now cannot support native method " + jvmMethod);
        }

        // make a new frame of this method
        Frame methodFrame = new Frame(
                frame.getJvmThread(),
                jvmMethod,
                localVariables
        );
        // before invoke new method, we need to save pc in current method
        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
        // now push the new frame
        frame.getJvmThread().pushFrame(methodFrame);
    }

    @Override
    public int size() {
        return 5;
    }

    /**
     * unsigned
     * (indexbyte1 << 8) | indexbyte2
     * @return
     */
    public int resolveIndex() {
        short shortIndex = ByteUtils.bytes2short(indexByte1, indexByte2);
        return PrimitiveTypeUtils.intFormUnsignedShort(shortIndex);
    }
}
