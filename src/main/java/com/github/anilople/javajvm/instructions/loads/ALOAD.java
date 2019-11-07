package com.github.anilople.javajvm.instructions.loads;

import com.github.anilople.javajvm.instructions.BytecodeReader;
import com.github.anilople.javajvm.instructions.Instruction;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;

/**
 * Operation
 * Load reference from local variable
 * <p>
 * Operand ... →
 * Stack ..., objectref
 * <p>
 * Description
 * The index is an unsigned byte that must be an index into the local
 * variable array of the current frame (§2.6). The local variable at
 * index must contain a reference . The objectref in the local variable
 * at index is pushed onto the operand stack.
 * <p>
 * Notes
 * The aload instruction cannot be used to load a value of type
 * returnAddress from a local variable onto the operand stack. This
 * asymmetry with the astore instruction (§astore) is intentional.
 * The aload opcode can be used in conjunction with the wide
 * instruction (§wide) to access a local variable using a two-byte
 * unsigned index.
 */
public class ALOAD implements Instruction {

    private byte index;

    @Override
    public void fetchOperands(BytecodeReader bytecodeReader) {
        bytecodeReader.readU1();
    }

    @Override
    public int execute(Frame frame) {
        short shortIndex = PrimitiveTypeUtils.shortFormUnsignedByte(index);

        // may add the check here
        Reference reference = frame.getLocalVariables().getReference(index);
        frame.getOperandStacks().pushReference(reference);

        return frame.getJvmThread().getPc() + this.size();
    }

    @Override
    public int size() {
        return 2;
    }

}
