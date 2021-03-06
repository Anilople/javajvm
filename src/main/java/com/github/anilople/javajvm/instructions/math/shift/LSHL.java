package com.github.anilople.javajvm.instructions.math.shift;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

/**
 * Operation
 * Shift left long
 *
 * Operand ..., value1, value2 →
 * Stack ..., result
 *
 * Description
 * The value1 must be of type long , and value2 must be of type int .
 * The values are popped from the operand stack. A long result is
 * calculated by shifting value1 left by s bit positions, where s is the
 * low 6 bits of value2. The result is pushed onto the operand stack.
 *
 * Notes
 * This is equivalent (even if overflow occurs) to multiplication by 2
 * to the power s. The shift distance actually used is therefore always
 * in the range 0 to 63, inclusive, as if value2 were subjected to a
 * bitwise logical AND with the mask value 0x3f.
 */
public class LSHL implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void execute(Frame frame) {
        final int value2 = frame.getOperandStacks().popIntValue();
        final long value1 = frame.getOperandStacks().popLongValue();

        final int s = value2 & 0b0011_1111; // same as (value2 & 0x3f)
        final long result = value1 << s;
        frame.getOperandStacks().pushLongValue(result);

        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 1;
    }

}
