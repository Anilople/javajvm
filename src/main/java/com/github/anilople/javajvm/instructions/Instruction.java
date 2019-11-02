package com.github.anilople.javajvm.instructions;

import com.github.anilople.javajvm.instructions.comparisons.*;
import com.github.anilople.javajvm.instructions.constants.*;
import com.github.anilople.javajvm.instructions.control.*;
import com.github.anilople.javajvm.instructions.conversions.*;
import com.github.anilople.javajvm.instructions.extended.*;
import com.github.anilople.javajvm.instructions.loads.*;
import com.github.anilople.javajvm.instructions.math.*;
import com.github.anilople.javajvm.instructions.math.add.*;
import com.github.anilople.javajvm.instructions.math.bitwiseand.*;
import com.github.anilople.javajvm.instructions.math.bitwiseexclusiveor.*;
import com.github.anilople.javajvm.instructions.math.bitwiseor.*;
import com.github.anilople.javajvm.instructions.math.divide.*;
import com.github.anilople.javajvm.instructions.math.multiply.*;
import com.github.anilople.javajvm.instructions.math.negate.*;
import com.github.anilople.javajvm.instructions.math.remainder.*;
import com.github.anilople.javajvm.instructions.math.shift.*;
import com.github.anilople.javajvm.instructions.math.subtract.*;
import com.github.anilople.javajvm.instructions.references.*;
import com.github.anilople.javajvm.instructions.reserved.*;
import com.github.anilople.javajvm.instructions.stack.*;
import com.github.anilople.javajvm.instructions.stores.*;
import com.github.anilople.javajvm.runtimedataarea.Frame;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;

/**
 * any instruction must implement this interface
 */
public interface Instruction {

    /**
     * some instruct fetch no operand
     * other fetch some operands
     * @param bytecodeReader
     */
    void FetchOperands(BytecodeReader bytecodeReader);

    /**
     * the execution of byte code dependencies the environment
     * i.e a stack frame
     * @param frame
     */
    void Execute(Frame frame);

    static Instruction readInstruction(BytecodeReader bytecodeReader) {
        byte opcode = bytecodeReader.readU1();
        int unsignedOpcode = PrimitiveTypeUtils.intFormUnsignedByte(opcode);
        switch (unsignedOpcode) {
            case 0: // 0x0
                return new NOP();
            case 1: // 0x1
                return new ACONST_NULL();
            case 2: // 0x2
                return new ICONST_M1();
            case 3: // 0x3
                return new ICONST_0();
            case 4: // 0x4
                return new ICONST_1();
            case 5: // 0x5
                return new ICONST_2();
            case 6: // 0x6
                return new ICONST_3();
            case 7: // 0x7
                return new ICONST_4();
            case 8: // 0x8
                return new ICONST_5();
            case 9: // 0x9
                return new LCONST_0();
            case 10: // 0xa
                return new LCONST_1();
            case 11: // 0xb
                return new FCONST_0();
            case 12: // 0xc
                return new FCONST_1();
            case 13: // 0xd
                return new FCONST_2();
            case 14: // 0xe
                return new DCONST_0();
            case 15: // 0xf
                return new DCONST_1();
            case 16: // 0x10
                return new BIPUSH();
            case 17: // 0x11
                return new SIPUSH();
            case 18: // 0x12
                return new LDC();
            case 19: // 0x13
                return new LDC_W();
            case 20: // 0x14
                return new LDC2_W();
            case 21: // 0x15
                return new ILOAD();
            case 22: // 0x16
                return new LLOAD();
            case 23: // 0x17
                return new FLOAD();
            case 24: // 0x18
                return new DLOAD();
            case 25: // 0x19
                return new ALOAD();
            case 26: // 0x1a
                return new ILOAD_0();
            case 27: // 0x1b
                return new ILOAD_1();
            case 28: // 0x1c
                return new ILOAD_2();
            case 29: // 0x1d
                return new ILOAD_3();
            case 30: // 0x1e
                return new LLOAD_0();
            case 31: // 0x1f
                return new LLOAD_1();
            case 32: // 0x20
                return new LLOAD_2();
            case 33: // 0x21
                return new LLOAD_3();
            case 34: // 0x22
                return new FLOAD_0();
            case 35: // 0x23
                return new FLOAD_1();
            case 36: // 0x24
                return new FLOAD_2();
            case 37: // 0x25
                return new FLOAD_3();
            case 38: // 0x26
                return new DLOAD_0();
            case 39: // 0x27
                return new DLOAD_1();
            case 40: // 0x28
                return new DLOAD_2();
            case 41: // 0x29
                return new DLOAD_3();
            case 42: // 0x2a
                return new ALOAD_0();
            case 43: // 0x2b
                return new ALOAD_1();
            case 44: // 0x2c
                return new ALOAD_2();
            case 45: // 0x2d
                return new ALOAD_3();
            case 46: // 0x2e
                return new IALOAD();
            case 47: // 0x2f
                return new LALOAD();
            case 48: // 0x30
                return new FALOAD();
            case 49: // 0x31
                return new DALOAD();
            case 50: // 0x32
                return new AALOAD();
            case 51: // 0x33
                return new BALOAD();
            case 52: // 0x34
                return new CALOAD();
            case 53: // 0x35
                return new SALOAD();
            case 54: // 0x36
                return new ISTORE();
            case 55: // 0x37
                return new LSTORE();
            case 56: // 0x38
                return new FSTORE();
            case 57: // 0x39
                return new DSTORE();
            case 58: // 0x3a
                return new ASTORE();
            case 59: // 0x3b
                return new ISTORE_0();
            case 60: // 0x3c
                return new ISTORE_1();
            case 61: // 0x3d
                return new ISTORE_2();
            case 62: // 0x3e
                return new ISTORE_3();
            case 63: // 0x3f
                return new LSTORE_0();
            case 64: // 0x40
                return new LSTORE_1();
            case 65: // 0x41
                return new LSTORE_2();
            case 66: // 0x42
                return new LSTORE_3();
            case 67: // 0x43
                return new FSTORE_0();
            case 68: // 0x44
                return new FSTORE_1();
            case 69: // 0x45
                return new FSTORE_2();
            case 70: // 0x46
                return new FSTORE_3();
            case 71: // 0x47
                return new DSTORE_0();
            case 72: // 0x48
                return new DSTORE_1();
            case 73: // 0x49
                return new DSTORE_2();
            case 74: // 0x4a
                return new DSTORE_3();
            case 75: // 0x4b
                return new ASTORE_0();
            case 76: // 0x4c
                return new ASTORE_1();
            case 77: // 0x4d
                return new ASTORE_2();
            case 78: // 0x4e
                return new ASTORE_3();
            case 79: // 0x4f
                return new IASTORE();
            case 80: // 0x50
                return new LASTORE();
            case 81: // 0x51
                return new FASTORE();
            case 82: // 0x52
                return new DASTORE();
            case 83: // 0x53
                return new AASTORE();
            case 84: // 0x54
                return new BASTORE();
            case 85: // 0x55
                return new CASTORE();
            case 86: // 0x56
                return new SASTORE();
            case 87: // 0x57
                return new POP();
            case 88: // 0x58
                return new POP2();
            case 89: // 0x59
                return new DUP();
            case 90: // 0x5a
                return new DUP_X1();
            case 91: // 0x5b
                return new DUP_X2();
            case 92: // 0x5c
                return new DUP2();
            case 93: // 0x5d
                return new DUP2_X1();
            case 94: // 0x5e
                return new DUP2_X2();
            case 95: // 0x5f
                return new SWAP();
            case 96: // 0x60
                return new IADD();
            case 97: // 0x61
                return new LADD();
            case 98: // 0x62
                return new FADD();
            case 99: // 0x63
                return new DADD();
            case 100: // 0x64
                return new ISUB();
            case 101: // 0x65
                return new LSUB();
            case 102: // 0x66
                return new FSUB();
            case 103: // 0x67
                return new DSUB();
            case 104: // 0x68
                return new IMUL();
            case 105: // 0x69
                return new LMUL();
            case 106: // 0x6a
                return new FMUL();
            case 107: // 0x6b
                return new DMUL();
            case 108: // 0x6c
                return new IDIV();
            case 109: // 0x6d
                return new LDIV();
            case 110: // 0x6e
                return new FDIV();
            case 111: // 0x6f
                return new DDIV();
            case 112: // 0x70
                return new IREM();
            case 113: // 0x71
                return new LREM();
            case 114: // 0x72
                return new FREM();
            case 115: // 0x73
                return new DREM();
            case 116: // 0x74
                return new INEG();
            case 117: // 0x75
                return new LNEG();
            case 118: // 0x76
                return new FNEG();
            case 119: // 0x77
                return new DNEG();
            case 120: // 0x78
                return new ISHL();
            case 121: // 0x79
                return new LSHL();
            case 122: // 0x7a
                return new ISHR();
            case 123: // 0x7b
                return new LSHR();
            case 124: // 0x7c
                return new IUSHR();
            case 125: // 0x7d
                return new LUSHR();
            case 126: // 0x7e
                return new IAND();
            case 127: // 0x7f
                return new LAND();
            case 128: // 0x80
                return new IOR();
            case 129: // 0x81
                return new LOR();
            case 130: // 0x82
                return new IXOR();
            case 131: // 0x83
                return new LXOR();
            case 132: // 0x84
                return new IINC();
            case 133: // 0x85
                return new I2L();
            case 134: // 0x86
                return new I2F();
            case 135: // 0x87
                return new I2D();
            case 136: // 0x88
                return new L2I();
            case 137: // 0x89
                return new L2F();
            case 138: // 0x8a
                return new L2D();
            case 139: // 0x8b
                return new F2I();
            case 140: // 0x8c
                return new F2L();
            case 141: // 0x8d
                return new F2D();
            case 142: // 0x8e
                return new D2I();
            case 143: // 0x8f
                return new D2L();
            case 144: // 0x90
                return new D2F();
            case 145: // 0x91
                return new I2B();
            case 146: // 0x92
                return new I2C();
            case 147: // 0x93
                return new I2S();
            case 148: // 0x94
                return new LCMP();
            case 149: // 0x95
                return new FCMPL();
            case 150: // 0x96
                return new FCMPG();
            case 151: // 0x97
                return new DCMPL();
            case 152: // 0x98
                return new DCMPG();
            case 153: // 0x99
                return new IFEQ();
            case 154: // 0x9a
                return new IFNE();
            case 155: // 0x9b
                return new IFLT();
            case 156: // 0x9c
                return new IFGE();
            case 157: // 0x9d
                return new IFGT();
            case 158: // 0x9e
                return new IFLE();
            case 159: // 0x9f
                return new IF_ICMPEQ();
            case 160: // 0xa0
                return new IF_ICMPNE();
            case 161: // 0xa1
                return new IF_ICMPLT();
            case 162: // 0xa2
                return new IF_ICMPGE();
            case 163: // 0xa3
                return new IF_ICMPGT();
            case 164: // 0xa4
                return new IF_ICMPLE();
            case 165: // 0xa5
                return new IF_ACMPEQ();
            case 166: // 0xa6
                return new IF_ACMPNE();
            case 167: // 0xa7
                return new GOTO();
            case 168: // 0xa8
                return new JSR();
            case 169: // 0xa9
                return new RET();
            case 170: // 0xaa
                return new TABLESWITCH();
            case 171: // 0xab
                return new LOOKUPSWITCH();
            case 172: // 0xac
                return new IRETURN();
            case 173: // 0xad
                return new LRETURN();
            case 174: // 0xae
                return new FRETURN();
            case 175: // 0xaf
                return new DRETURN();
            case 176: // 0xb0
                return new ARETURN();
            case 177: // 0xb1
                return new RETURN();
            case 178: // 0xb2
                return new GETSTATIC();
            case 179: // 0xb3
                return new PUTSTATIC();
            case 180: // 0xb4
                return new GETFIELD();
            case 181: // 0xb5
                return new PUTFIELD();
            case 182: // 0xb6
                return new INVOKEVIRTUAL();
            case 183: // 0xb7
                return new INVOKESPECIAL();
            case 184: // 0xb8
                return new INVOKESTATIC();
            case 185: // 0xb9
                return new INVOKEINTERFACE();
            case 186: // 0xba
                return new INVOKEDYNAMIC();
            case 187: // 0xbb
                return new NEW();
            case 188: // 0xbc
                return new NEWARRAY();
            case 189: // 0xbd
                return new ANEWARRAY();
            case 190: // 0xbe
                return new ARRAYLENGTH();
            case 191: // 0xbf
                return new ATHROW();
            case 192: // 0xc0
                return new CHECKCAST();
            case 193: // 0xc1
                return new INSTANCEOF();
            case 194: // 0xc2
                return new MONITORENTER();
            case 195: // 0xc3
                return new MONITOREXIT();
            case 196: // 0xc4
                return new WIDE();
            case 197: // 0xc5
                return new MULTIANEWARRAY();
            case 198: // 0xc6
                return new IFNULL();
            case 199: // 0xc7
                return new IFNONNULL();
            case 200: // 0xc8
                return new GOTO_W();
            case 201: // 0xc9
                return new JSR_W();
            case 202: // 0xca
                return new BREAKPOINT();
            case 254: // 0xfe
                return new IMPDEP1();
            case 255: // 0xff
                return new IMPDEP2();
            default:
                throw new IllegalStateException("Unexpected value: " + unsignedOpcode);
        }
    }

}
