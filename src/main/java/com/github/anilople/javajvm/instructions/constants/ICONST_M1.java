package com.github.anilople.javajvm.instructions.constants;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

/**
 * Description: Push the int constant <i> (-1, 0, 1, 2, 3, 4 or 5) onto the operand
 * stack.
 * Notes: Each of this family of instructions is equivalent to bipush <i> for
 * the respective value of <i>, except that the operand <i> is implicit.
 */
public class ICONST_M1 implements Instruction {

    /**
     * push the int constant i onto the operand stack
     * @param instruction
     * @param frame
     * @param i
     * @return
     */
    public static int execute(Instruction instruction, Frame frame, int i) {
        frame.getOperandStacks().pushIntValue(i);
        int nextPc = frame.getNextPc() + instruction.size();
        frame.setNextPc(nextPc);
        return frame.getJvmThread().getPc() + instruction.size();
    }

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public int execute(Frame frame) {
        return ICONST_M1.execute(this, frame, -1);
    }

    @Override
    public int size() {
        return 1;
    }
}
