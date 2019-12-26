package com.github.anilople.javajvm.instructions.math.add;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;

/**
 * Operation Add double
 *
 * Operand ..., value1, value2 →
 * Stack ..., result
 *
 * Description
 * Both value1 and value2 must be of type double . The values are
 * popped from the operand stack and undergo value set conversion
 * (§2.8.3), resulting in value1' and value2'. The double result is
 * value1' + value2'. The result is pushed onto the operand stack.
 * The result of a dadd instruction is governed by the rules of IEEE
 * arithmetic:
 * ......
 *
 * The Java Virtual Machine requires support of gradual underflow
 * as defined by IEEE 754. Despite the fact that overflow, underflow,
 * or loss of precision may occur, execution of a dadd instruction
 * never throws a run-time exception.
 */
public class DADD implements Instruction {

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {

    }

    @Override
    public void execute(Frame frame) {

        double value2 = frame.getOperandStacks().popDoubleValue();
        double value1 = frame.getOperandStacks().popDoubleValue();

        double result = Double.sum(value1, value2);

        frame.getOperandStacks().pushDoubleValue(result);

        int nextPc = frame.getNextPc() + this.size();
        frame.setNextPc(nextPc);
    }

    @Override
    public int size() {
        return 1;
    }

}
