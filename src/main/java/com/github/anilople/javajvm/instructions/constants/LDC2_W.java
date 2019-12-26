package com.github.anilople.javajvm.instructions.constants;

import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.heap.constant.JvmConstant;
import com.github.anilople.javajvm.heap.constant.JvmConstantDouble;
import com.github.anilople.javajvm.heap.constant.JvmConstantLong;
import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.utils.ByteUtils;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;

/**
 * Operation
 * Push long or double from run-time constant pool (wide index)
 *
 * Operand ... →
 * Stack ..., value
 *
 * Description
 * The unsigned indexbyte1 and indexbyte2 are assembled into an
 * unsigned 16-bit index into the run-time constant pool of the
 * current class (§2.6), where the value of the index is calculated as
 * (indexbyte1 << 8) | indexbyte2. The index must be a valid index
 * into the run-time constant pool of the current class. The run-time
 * constant pool entry at the index must be a run-time constant of
 * type long or double (§5.1). The numeric value of that run-time
 * constant is pushed onto the operand stack as a long or double ,
 * respectively.
 *
 * Notes
 * Only a wide-index version of the ldc2_w instruction exists; there
 * is no ldc2 instruction that pushes a long or double with a single-
 * byte index.
 *
 * The ldc2_w instruction can only be used to push a value of type
 * double taken from the double value set (§2.3.2) because a constant
 * of type double in the constant pool (§4.4.5) must be taken from
 * the double value set.
 */
public class LDC2_W implements Instruction {

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
        JvmClass currentClass = frame.getJvmMethod().getJvmClass();
        JvmConstant jvmConstant = currentClass.getJvmConstantPool().getJvmConstant(index);
        if(jvmConstant instanceof JvmConstantLong) {
            JvmConstantLong jvmConstantLong = (JvmConstantLong) jvmConstant;
            // get the value
            long value = jvmConstantLong.resolveValue();
            // push it to the operand stack
            frame.getOperandStacks().pushLongValue(value);
        } else if(jvmConstant instanceof JvmConstantDouble) {
            JvmConstantDouble jvmConstantDouble = (JvmConstantDouble) jvmConstant;
            throw new RuntimeException(LDC2_W.class + " now cannot support " + jvmConstantDouble);
        } else {
            throw new RuntimeException(LDC2_W.class + " cannot support " + jvmConstant);
        }

        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 3;
    }
}
