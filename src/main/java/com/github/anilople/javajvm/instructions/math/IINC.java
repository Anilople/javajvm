package com.github.anilople.javajvm.instructions.math;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;

/**
 * Operation
 * Increment local variable by constant
 *
 * Operand No change
 * Stack
 *
 * Description
 * The index is an unsigned byte that must be an index into the
 * local variable array of the current frame (§2.6). The const is an
 * immediate signed byte. The local variable at index must contain
 * an int . The value const is first sign-extended to an int , and then
 * the local variable at index is incremented by that amount.
 *
 * Notes
 * The iinc opcode can be used in conjunction with the wide
 * instruction (§wide) to access a local variable using a two-byte
 * unsigned index and to increment it by a two-byte immediate signed
 * value.
 */
public class IINC implements Instruction {

    private byte index;

    private byte signedByteConst;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.index = bytecodeReader.readU1();
        this.signedByteConst = bytecodeReader.readU1();
    }

    @Override
    public int execute(Frame frame) {

        int intFormIndex = PrimitiveTypeUtils.intFormUnsignedByte(this.index);

        // the operation is not atomic!!!
        int constValue = PrimitiveTypeUtils.intFormSignedByte(this.signedByteConst);
        int intValue = frame.getLocalVariables().getIntValue(intFormIndex);
        frame.getLocalVariables().setIntValue(intFormIndex, intValue + constValue);

        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
        return frame.getJvmThread().getPc() + this.size();
    }

    @Override
    public int size() {
        return 3;
    }

}
