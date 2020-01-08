package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.constants.Descriptors;
import com.github.anilople.javajvm.heap.JvmMethod;
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

import java.util.Collections;
import java.util.List;

public class INVOKESTATIC implements Instruction {

    private static final Logger logger = LoggerFactory.getLogger(INVOKESTATIC.class);

    private byte indexByte1;

    private byte indexByte2;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.indexByte1 = bytecodeReader.readU1();
        this.indexByte2 = bytecodeReader.readU1();
    }

    @Override
    public void execute(Frame frame) {
        int index = PrimitiveTypeUtils.intFormUnsignedShort(ByteUtils.bytes2short(indexByte1, indexByte2));

        // ignore interface method
        JvmConstantMethodref jvmConstantMethodref = (JvmConstantMethodref) frame.getJvmMethod().getJvmClass().getJvmConstantPool().getJvmConstant(index);
        JvmMethod jvmMethod = jvmConstantMethodref.resolveJvmMethod();
        logger.trace("jvm class: {}, method: {}", jvmMethod.getJvmClass().getName(), jvmMethod);

        //
        if(!jvmMethod.isStatic()) {
            throw new IncompatibleClassChangeError("method is not static");
        }


        String methodDescriptor = jvmMethod.getDescriptor();
        logger.trace("method descriptor: {}", methodDescriptor);
        List<String> parameterDescriptors = DescriptorUtils.getParameterDescriptor(methodDescriptor);
        // pop args and object reference
        LocalVariables localVariables = DescriptorUtils.popArgsByParameterDescriptor(
                false,
                frame.getOperandStacks(),
                parameterDescriptors
        );

        // use hack skill
        if(HackUtils.isInHackMethods(jvmMethod)) {
            HackUtils.hackStaticNativeMethod(jvmMethod, localVariables);
            // early return here
            int nextPc = frame.getNextPc() + this.size();
            frame.setNextPc(nextPc);
            return;
        }

        // native method check
        if(jvmMethod.isNative()) {
            logger.debug("class {}, native method: {}, {}", jvmMethod.getJvmClass().getName(), jvmMethod.getName(), jvmMethod.getDescriptor());
            // check register or not, to do
            // early return here
            int nextPc = frame.getNextPc() + this.size();
            frame.setNextPc(nextPc);
            return;
        }

        // make a new frame of this method
        Frame staticMethodFrame = new Frame(
                frame.getJvmThread(),
                jvmMethod,
                localVariables
        );
        // before invoke new method, we need to save pc in current method
        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
        // now push the new frame
        frame.getJvmThread().pushFrame(staticMethodFrame);

    }

    @Override
    public int size() {
        return 3;
    }

}
