package com.github.anilople.javajvm.instructions.references;

import com.github.anilople.javajvm.constants.Descriptors;
import com.github.anilople.javajvm.heap.JvmMethod;
import com.github.anilople.javajvm.heap.constant.JvmConstantMethodref;
import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.utils.ByteUtils;
import com.github.anilople.javajvm.utils.DescriptorUtils;
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
    public int execute(Frame frame) {
        int index = PrimitiveTypeUtils.intFormUnsignedShort(ByteUtils.bytes2short(indexByte1, indexByte2));

        // ignore interface method
        JvmConstantMethodref jvmConstantMethodref = (JvmConstantMethodref) frame.getJvmMethod().getJvmClass().getJvmConstantPool().getJvmConstant(index);
        JvmMethod jvmMethod = jvmConstantMethodref.resolveJvmField();
        logger.trace("jvm class: {}, method: {}", jvmMethod.getJvmClass().getName(), jvmMethod);
        if(jvmMethod.isNative()) {
            logger.debug("class {}, native method: {}, {}", jvmMethod.getJvmClass().getName(), jvmMethod.getName(), jvmMethod.getDescriptor());
            // check register or not, to do
            // early return here
            int nextPc = frame.getNextPc() + this.size();
            frame.setNextPc(nextPc);
            return frame.getJvmThread().getPc() + this.size();
        }

        //
        if(!jvmMethod.isStatic()) {
            throw new IncompatibleClassChangeError("method is not static");
        }

        String methodDescriptor = jvmMethod.getDescriptor();
        logger.trace("method descriptor: {}", methodDescriptor);
        if(!DescriptorUtils.isMethodDescriptor(methodDescriptor)) {
            throw new RuntimeException(methodDescriptor + " is not a method descriptor");
        }
        List<String> descriptorParameters = DescriptorUtils.getParameterDescriptor(methodDescriptor);
        logger.trace("method descriptor parameters: {}", descriptorParameters);

        // reverse list
        Collections.reverse(descriptorParameters);
        // new method frame, save local variable in this frame
        Frame staticMethodFrame = new Frame(
                frame.getJvmThread(),
                jvmMethod
        );
        int localVariableIndex = 0;
        for(String descriptorOne : descriptorParameters) {
            logger.trace("one parameter: {}", descriptorOne);
            if(DescriptorUtils.isBaseType(descriptorOne)) {
                switch (descriptorOne) {
                    case Descriptors.BaseType.BOOLEAN:
                    case Descriptors.BaseType.BYTE:
                    case Descriptors.BaseType.CHAR:
                    case Descriptors.BaseType.SHORT:
                    case Descriptors.BaseType.INT:
                        int intValue = frame.getOperandStacks().popIntValue();
                        staticMethodFrame.getLocalVariables().setIntValue(localVariableIndex, intValue);
                        break;
                    case Descriptors.BaseType.FLOAT:
                        float floatValue = frame.getOperandStacks().popFloatValue();
                        staticMethodFrame.getLocalVariables().setFloatValue(localVariableIndex, floatValue);
                        break;
                    case Descriptors.BaseType.LONG:
                        long longValue = frame.getOperandStacks().popLongValue();
                        staticMethodFrame.getLocalVariables().setLongValue(localVariableIndex, longValue);
                        localVariableIndex += 1;
                        break;
                    case Descriptors.BaseType.DOUBLE:
                        double doubleValue = frame.getOperandStacks().popDoubleValue();
                        staticMethodFrame.getLocalVariables().setDoubleValue(localVariableIndex, doubleValue);
                        localVariableIndex += 1;
                }
                localVariableIndex += 1;
            } else if(DescriptorUtils.isObjectType(descriptorOne)) {
                Reference reference = frame.getOperandStacks().popReference();
                staticMethodFrame.getLocalVariables().setReference(localVariableIndex, reference);
                localVariableIndex += 1;
            } else if(DescriptorUtils.isArrayType(descriptorOne)) {
                throw new RuntimeException(descriptorOne + " array type not support now.");
            } else {
                throw new RuntimeException("What descriptor is " + descriptorOne);
            }
        }

        // add parameter finished
        // invoke static method, but we need to save now frame's nextPc first
        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
        // now push a new frame
        frame.getJvmThread().pushFrame(staticMethodFrame);

        return frame.getJvmThread().getPc() + this.size();
    }

    @Override
    public int size() {
        return 3;
    }

}
