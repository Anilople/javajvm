package com.github.anilople.javajvm.instructions.comparisons.ifinstructions;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.utils.ByteUtils;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;

public class IF_ICMPGE implements Instruction {

    private byte branchbyte1;

    private byte branchbyte2;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        this.branchbyte1 = bytecodeReader.readU1();
        this.branchbyte2 = bytecodeReader.readU1();
    }

    @Override
    public int execute(Frame frame) {
        int value2 = frame.getOperandStacks().popIntValue();
        int value1 = frame.getOperandStacks().popIntValue();
        //  if_icmpgt succeeds if and only if value1 > value2
        if(value1 >= value2) {
            // succeed
            short branchOffset = ByteUtils.bytes2short(branchbyte1, branchbyte2);
            int nextPc = frame.getNextPc() + PrimitiveTypeUtils.intFormSignedShort(branchOffset);
            frame.setNextPc(nextPc);
        } else {
            int nextPc = frame.getNextPc() + this.size();
            frame.setNextPc(nextPc);
        }
        return frame.getJvmThread().getPc() + this.size();
    }

    @Override
    public int size() {
        return 3;
    }

}
