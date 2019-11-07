package com.github.anilople.javajvm.instructions.loads;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

/**
 * Operation:
 * Load int from local variable
 * Description:
 * The <n> must be an index into the local variable array of the
 * current frame (§2.6). The local variable at <n> must contain an
 * int . The value of the local variable at <n> is pushed onto the
 * operand stack.
 * Notes:
 * Each of the iload_<n> instructions is the same as iload with an
 * index of <n>, except that the operand <n> is implicit.
 */
public class ILOAD_2 implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public int execute(Frame frame) {
        return ILOAD.execute(this, frame, 2);
    }

    @Override
    public int size() {
        return 1;
    }
}
