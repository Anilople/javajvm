package com.github.anilople.javajvm.instructions.math.shift;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

/**
 * Operation
 * Logical shift right long
 *
 * Operand ..., value1, value2 →
 * Stack ..., result
 *
 * Description
 * The value1 must be of type long , and value2 must be of type int .
 * The values are popped from the operand stack. A long result is
 * calculated by shifting value1 right logically by s bit positions, with
 * zero extension, where s is the value of the low 6 bits of value2.
 * The result is pushed onto the operand stack.
 *
 * Notes
 * If value1 is positive and s is value2 & 0x3f, the result is the same
 * as that of value1 >> s; if value1 is negative, the result is equal to the
 * value of the expression (value1 >> s) + (2L << ~s). The addition of
 * the (2L << ~s) term cancels out the propagated sign bit. The shift
 * distance actually used is always in the range 0 to 63, inclusive.
 */
public class LUSHR implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }


    @Override
    public void execute(Frame frame) {
        final int value2 = frame.getOperandStacks().popIntValue();
        final long value1 = frame.getOperandStacks().popLongValue();

        final int s = value2 & 0b0011_1111; // same as (value2 & 0x3f)
        final long result = value1 >>> s;
        frame.getOperandStacks().pushLongValue(result);

        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 1;
    }

}
