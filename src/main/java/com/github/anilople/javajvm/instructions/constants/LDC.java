package com.github.anilople.javajvm.instructions.constants;

import com.github.anilople.javajvm.cachepool.StringPool;
import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.heap.JvmClassLoader;
import com.github.anilople.javajvm.heap.constant.*;
import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.BaseTypeArrayReference;
import com.github.anilople.javajvm.runtimedataarea.reference.ClassObjectReference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectReference;
import com.github.anilople.javajvm.utils.ClassNameConverterUtils;
import com.github.anilople.javajvm.utils.ConstantPoolUtils;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;
import com.github.anilople.javajvm.utils.ReferenceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Operation:
 * Push item from run-time constant pool (wide index)
 *
 * Operand ... →
 * Stack ..., value
 *
 * Description
 * The index is an unsigned byte that must be a valid index into the
 * run-time constant pool of the current class (§2.6).
 *
 * The run-time
 * constant pool entry at index either must be a run-time constant of
 * type int or float , or a reference to a string literal, or a symbolic
 * reference to a class, method type, or method handle (§5.1).
 *
 * Otherwise, if the run-time constant pool entry is a symbolic
 * reference to a class (§5.1), then the named class is resolved
 * (§5.4.3.1) and a reference to the Class object representing that
 * class, value, is pushed onto the operand stack.
 *
 * Otherwise, the run-time constant pool entry must be a symbolic
 * reference to a method type or a method handle (§5.1). The method
 * type or method handle is resolved (§5.4.3.5) and a reference
 * to the resulting instance of java.lang.invoke.MethodType or
 * java.lang.invoke.MethodHandle , value, is pushed onto the
 * operand stack.
 *
 *
 */
public class LDC implements Instruction {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private byte unsignedByteIndex;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.unsignedByteIndex = bytecodeReader.readU1();
    }

    @Override
    public void execute(Frame frame) {
        JvmClass currentClass = frame.getJvmMethod().getJvmClass();
        int index = PrimitiveTypeUtils.intFormUnsignedByte(unsignedByteIndex);
        JvmConstant jvmConstant = currentClass.getJvmConstantPool().getJvmConstant(index);
        logger.info("jvmConstant: {}", jvmConstant);
        if(jvmConstant instanceof JvmConstantInteger) {
            int intValue = ((JvmConstantInteger) jvmConstant).getIntValue();
            frame.getOperandStacks().pushIntValue(intValue);
        } else if(jvmConstant instanceof JvmConstantFloat) {
            float floatValue = ((JvmConstantFloat) jvmConstant).getFloatValue();
            frame.getOperandStacks().pushFloatValue(floatValue);
        } else if(jvmConstant instanceof JvmConstantString) {
            // a reference to a string literal
//            throw new RuntimeException("LDC now cannot support JvmConstantString " + jvmConstant);
            JvmConstantString jvmConstantString = (JvmConstantString) jvmConstant;
            String utf8 = jvmConstantString.getJvmClass().getJvmConstantPool().getUtf8String(
                    jvmConstantString.getConstantStringInfo().getStringIndex()
            );
            logger.trace("String content = {}", utf8);
            // get string by tool
            ObjectReference objectReference = ReferenceUtils.getStringObjectReference(
                    currentClass.getLoader().loadClass(String.class),
                    utf8
            );
            frame.getOperandStacks().pushReference(objectReference);
//            throw new RuntimeException("LDC now cannot support " + jvmConstant);
        } else if(jvmConstant instanceof JvmConstantClass) {
            JvmConstantClass jvmConstantClass = (JvmConstantClass) jvmConstant;
            JvmClass jvmClass = jvmConstantClass.resolveJvmClass();
            ClassObjectReference classObjectReference = ClassObjectReference.getInstance(jvmClass);
            frame.getOperandStacks().pushReference(classObjectReference);
        } else if(jvmConstant instanceof JvmConstantMethodType) {
            JvmConstantMethodType jvmConstantMethodType = (JvmConstantMethodType) jvmConstant;
            throw new RuntimeException("LDC now cannot support " + jvmConstantMethodType);
        } else if(jvmConstant instanceof JvmConstantMethodHandle) {
            JvmConstantMethodHandle jvmConstantMethodHandle = (JvmConstantMethodHandle) jvmConstant;
            throw new RuntimeException("LDC now cannot support " + jvmConstantMethodHandle);
        } else {
            throw new IllegalArgumentException("LDC cannot support " + jvmConstant.toString());
        }
        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 2;
    }
}
