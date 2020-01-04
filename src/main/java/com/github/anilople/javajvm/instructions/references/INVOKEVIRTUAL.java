package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.heap.JvmMethod;
import com.github.anilople.javajvm.heap.constant.JvmConstant;
import com.github.anilople.javajvm.heap.constant.JvmConstantMethodref;
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
 * Invoke instance method; dispatch based on class
 *
 * Operand ..., objectref, [arg1, [arg2 ...]] →
 * Stack ...
 *
 * Description
 * The unsigned indexbyte1 and indexbyte2 are used to construct an
 * index into the run-time constant pool of the current class (§2.6),
 * where the value of the index is (indexbyte1 << 8) | indexbyte2.
 * The run-time constant pool item at that index must be a symbolic
 * reference to a method (§5.1), which gives the name and descriptor
 * (§4.3.3) of the method as well as a symbolic reference to the class
 * in which the method is to be found. The named method is resolved
 * (§5.4.3.3).
 *
 * The resolved method must not be an instance initialization method,
 * or the class or interface initialization method (§2.9).
 *
 * If the resolved method is protected , and it is a member of a
 * superclass of the current class, and the method is not declared in
 * the same run-time package (§5.3) as the current class, then the
 * class of objectref must be either the current class or a subclass of
 * the current class.
 *
 *
 */
public class INVOKEVIRTUAL implements Instruction {

    private static final Logger logger = LoggerFactory.getLogger(INVOKEVIRTUAL.class);

    private byte indexByte1;

    private byte indexByte2;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.indexByte1 = bytecodeReader.readU1();
        this.indexByte2 = bytecodeReader.readU1();
    }

    @Override
    public void execute(Frame frame) {
        int index = this.resolveIndex();
        JvmConstant jvmConstant = frame.getJvmMethod().getJvmClass().getJvmConstantPool().getJvmConstant(index);
        JvmConstantMethodref jvmConstantMethodref = (JvmConstantMethodref) jvmConstant;
        JvmMethod jvmMethod = jvmConstantMethodref.resolveJvmMethod();
        if(jvmMethod.isStatic()) {
            throw new IncompatibleClassChangeError(jvmMethod.getName() + " " + ((JvmConstantMethodref) jvmConstant).getDescriptor());
        }

        String methodDescriptor = jvmMethod.getDescriptor();
        logger.trace("method descriptor: {}", methodDescriptor);
        List<String> parameterDescriptors = DescriptorUtils.getParameterDescriptor(methodDescriptor);
        // pop args and object reference
        LocalVariables localVariables = DescriptorUtils.popArgsByParameterDescriptor(
                true,
                frame.getOperandStacks(),
                parameterDescriptors
        );

        // use hack skill
        if(HackUtils.isInHackMethods(jvmMethod)) {
            // hack with System.out
            HackUtils.hackSystemOut(jvmMethod, localVariables);
            // early return here
            int nextPc = frame.getNextPc() + this.size();
            frame.setNextPc(nextPc);
            return;
        }

        // get object reference which has pop already
        Reference reference = localVariables.getReference(0);
        Reference.assertIsNotNull(reference);
        ObjectReference objectReference = (ObjectReference) reference;
        // check the object ref, todo

        if(jvmMethod.isNative()) {
            logger.debug("class {}, native method: {}, {}", jvmMethod.getJvmClass().getName(), jvmMethod.getName(), jvmMethod.getDescriptor());
            // check register or not, to do
            // early return here
            int nextPc = frame.getNextPc() + this.size();
            frame.setNextPc(nextPc);
            return;
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
        return 3;
    }

    public int resolveIndex() {
        short shortIndex = ByteUtils.bytes2short(indexByte1, indexByte2);
        return PrimitiveTypeUtils.intFormUnsignedShort(shortIndex);
    }

}
