package com.github.anilople.javajvm.instructions;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.instructions.constants.*;
import com.github.anilople.javajvm.instructions.loads.ILOAD;
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
            case 0 : // 0x0
                return new NOP();
            case 1 : // 0x1
                return new ACONST_NULL();
            case 2 : // 0x2
                return new ICONST_M1();
            case 3 : // 0x3
                return new ICONST_0();
            case 4 : // 0x4
                return new ICONST_1();
            case 5 : // 0x5
                return new ICONST_2();
            case 6 : // 0x6
                return new ICONST_3();
            case 7 : // 0x7
                return new ICONST_4();
            case 8 : // 0x8
                return new ICONST_5();
            case 9 : // 0x9
                return new LCONST_0();
            case 10 : // 0xa
                return new LCONST_1();
            case 11 : // 0xb
                return new FCONST_0();
            case 12 : // 0xc
                return new FCONST_1();
            case 13 : // 0xd
                return new FCONST_2();
            case 14 : // 0xe
                return new DCONST_0();
            case 15 : // 0xf
                return new DCONST_1();
            case 16 : // 0x10
                return new BIPUSH();
            case 17 : // 0x11
                return new SIPUSH();
            case 18 : // 0x12
                return new LDC();
            case 19 : // 0x13
                return new LDC_W();
            case 20 : // 0x14
                return new LDC2_W();
            case 21 : // 0x15
                return new ILOAD();
            case 22 : // 0x16
                return null;
            case 23 : // 0x17
                return null;
            case 24 : // 0x18
                return null;
            case 25 : // 0x19
                return null;
            case 26 : // 0x1a
                return null;
            case 27 : // 0x1b
                return null;
            case 28 : // 0x1c
                return null;
            case 29 : // 0x1d
                return null;
            case 30 : // 0x1e
                return null;
            case 31 : // 0x1f
                return null;
            case 32 : // 0x20
                return null;
            case 33 : // 0x21
                return null;
            case 34 : // 0x22
                return null;
            case 35 : // 0x23
                return null;
            case 36 : // 0x24
                return null;
            case 37 : // 0x25
                return null;
            case 38 : // 0x26
                return null;
            case 39 : // 0x27
                return null;
            case 40 : // 0x28
                return null;
            case 41 : // 0x29
                return null;
            case 42 : // 0x2a
                return null;
            case 43 : // 0x2b
                return null;
            case 44 : // 0x2c
                return null;
            case 45 : // 0x2d
                return null;
            case 46 : // 0x2e
                return null;
            case 47 : // 0x2f
                return null;
            case 48 : // 0x30
                return null;
            case 49 : // 0x31
                return null;
            case 50 : // 0x32
                return null;
            case 51 : // 0x33
                return null;
            case 52 : // 0x34
                return null;
            case 53 : // 0x35
                return null;
            case 54 : // 0x36
                return null;
            case 55 : // 0x37
                return null;
            case 56 : // 0x38
                return null;
            case 57 : // 0x39
                return null;
            case 58 : // 0x3a
                return null;
            case 59 : // 0x3b
                return null;
            case 60 : // 0x3c
                return null;
            case 61 : // 0x3d
                return null;
            case 62 : // 0x3e
                return null;
            case 63 : // 0x3f
                return null;
            case 64 : // 0x40
                return null;
            case 65 : // 0x41
                return null;
            case 66 : // 0x42
                return null;
            case 67 : // 0x43
                return null;
            case 68 : // 0x44
                return null;
            case 69 : // 0x45
                return null;
            case 70 : // 0x46
                return null;
            case 71 : // 0x47
                return null;
            case 72 : // 0x48
                return null;
            case 73 : // 0x49
                return null;
            case 74 : // 0x4a
                return null;
            case 75 : // 0x4b
                return null;
            case 76 : // 0x4c
                return null;
            case 77 : // 0x4d
                return null;
            case 78 : // 0x4e
                return null;
            case 79 : // 0x4f
                return null;
            case 80 : // 0x50
                return null;
            case 81 : // 0x51
                return null;
            case 82 : // 0x52
                return null;
            case 83 : // 0x53
                return null;
            case 84 : // 0x54
                return null;
            case 85 : // 0x55
                return null;
            case 86 : // 0x56
                return null;
            case 87 : // 0x57
                return null;
            case 88 : // 0x58
                return null;
            case 89 : // 0x59
                return null;
            case 90 : // 0x5a
                return null;
            case 91 : // 0x5b
                return null;
            case 92 : // 0x5c
                return null;
            case 93 : // 0x5d
                return null;
            case 94 : // 0x5e
                return null;
            case 95 : // 0x5f
                return null;
            case 96 : // 0x60
                return null;
            case 97 : // 0x61
                return null;
            case 98 : // 0x62
                return null;
            case 99 : // 0x63
                return null;
            case 100 : // 0x64
                return null;
            case 101 : // 0x65
                return null;
            case 102 : // 0x66
                return null;
            case 103 : // 0x67
                return null;
            case 104 : // 0x68
                return null;
            case 105 : // 0x69
                return null;
            case 106 : // 0x6a
                return null;
            case 107 : // 0x6b
                return null;
            case 108 : // 0x6c
                return null;
            case 109 : // 0x6d
                return null;
            case 110 : // 0x6e
                return null;
            case 111 : // 0x6f
                return null;
            case 112 : // 0x70
                return null;
            case 113 : // 0x71
                return null;
            case 114 : // 0x72
                return null;
            case 115 : // 0x73
                return null;
            case 116 : // 0x74
                return null;
            case 117 : // 0x75
                return null;
            case 118 : // 0x76
                return null;
            case 119 : // 0x77
                return null;
            case 120 : // 0x78
                return null;
            case 121 : // 0x79
                return null;
            case 122 : // 0x7a
                return null;
            case 123 : // 0x7b
                return null;
            case 124 : // 0x7c
                return null;
            case 125 : // 0x7d
                return null;
            case 126 : // 0x7e
                return null;
            case 127 : // 0x7f
                return null;
            case 128 : // 0x80
                return null;
            case 129 : // 0x81
                return null;
            case 130 : // 0x82
                return null;
            case 131 : // 0x83
                return null;
            case 132 : // 0x84
                return null;
            case 133 : // 0x85
                return null;
            case 134 : // 0x86
                return null;
            case 135 : // 0x87
                return null;
            case 136 : // 0x88
                return null;
            case 137 : // 0x89
                return null;
            case 138 : // 0x8a
                return null;
            case 139 : // 0x8b
                return null;
            case 140 : // 0x8c
                return null;
            case 141 : // 0x8d
                return null;
            case 142 : // 0x8e
                return null;
            case 143 : // 0x8f
                return null;
            case 144 : // 0x90
                return null;
            case 145 : // 0x91
                return null;
            case 146 : // 0x92
                return null;
            case 147 : // 0x93
                return null;
            case 148 : // 0x94
                return null;
            case 149 : // 0x95
                return null;
            case 150 : // 0x96
                return null;
            case 151 : // 0x97
                return null;
            case 152 : // 0x98
                return null;
            case 153 : // 0x99
                return null;
            case 154 : // 0x9a
                return null;
            case 155 : // 0x9b
                return null;
            case 156 : // 0x9c
                return null;
            case 157 : // 0x9d
                return null;
            case 158 : // 0x9e
                return null;
            case 159 : // 0x9f
                return null;
            case 160 : // 0xa0
                return null;
            case 161 : // 0xa1
                return null;
            case 162 : // 0xa2
                return null;
            case 163 : // 0xa3
                return null;
            case 164 : // 0xa4
                return null;
            case 165 : // 0xa5
                return null;
            case 166 : // 0xa6
                return null;
            case 167 : // 0xa7
                return null;
            case 168 : // 0xa8
                return null;
            case 169 : // 0xa9
                return null;
            case 170 : // 0xaa
                return null;
            case 171 : // 0xab
                return null;
            case 172 : // 0xac
                return null;
            case 173 : // 0xad
                return null;
            case 174 : // 0xae
                return null;
            case 175 : // 0xaf
                return null;
            case 176 : // 0xb0
                return null;
            case 177 : // 0xb1
                return null;
            case 178 : // 0xb2
                return null;
            case 179 : // 0xb3
                return null;
            case 180 : // 0xb4
                return null;
            case 181 : // 0xb5
                return null;
            case 182 : // 0xb6
                return null;
            case 183 : // 0xb7
                return null;
            case 184 : // 0xb8
                return null;
            case 185 : // 0xb9
                return null;
            case 186 : // 0xba
                return null;
            case 187 : // 0xbb
                return null;
            case 188 : // 0xbc
                return null;
            case 189 : // 0xbd
                return null;
            case 190 : // 0xbe
                return null;
            case 191 : // 0xbf
                return null;
            case 192 : // 0xc0
                return null;
            case 193 : // 0xc1
                return null;
            case 194 : // 0xc2
                return null;
            case 195 : // 0xc3
                return null;
            case 196 : // 0xc4
                return null;
            case 197 : // 0xc5
                return null;
            case 198 : // 0xc6
                return null;
            case 199 : // 0xc7
                return null;
            case 200 : // 0xc8
                return null;
            case 201 : // 0xc9
                return null;
            case 202 : // 0xca
                return null;
            case 203 : // 0xcb
                return null;
            case 204 : // 0xcc
                return null;
            case 205 : // 0xcd
                return null;
            case 206 : // 0xce
                return null;
            case 207 : // 0xcf
                return null;
            case 208 : // 0xd0
                return null;
            case 209 : // 0xd1
                return null;
            case 210 : // 0xd2
                return null;
            case 211 : // 0xd3
                return null;
            case 212 : // 0xd4
                return null;
            case 213 : // 0xd5
                return null;
            case 214 : // 0xd6
                return null;
            case 215 : // 0xd7
                return null;
            case 216 : // 0xd8
                return null;
            case 217 : // 0xd9
                return null;
            case 218 : // 0xda
                return null;
            case 219 : // 0xdb
                return null;
            case 220 : // 0xdc
                return null;
            case 221 : // 0xdd
                return null;
            case 222 : // 0xde
                return null;
            case 223 : // 0xdf
                return null;
            case 224 : // 0xe0
                return null;
            case 225 : // 0xe1
                return null;
            case 226 : // 0xe2
                return null;
            case 227 : // 0xe3
                return null;
            case 228 : // 0xe4
                return null;
            case 229 : // 0xe5
                return null;
            case 230 : // 0xe6
                return null;
            case 231 : // 0xe7
                return null;
            case 232 : // 0xe8
                return null;
            case 233 : // 0xe9
                return null;
            case 234 : // 0xea
                return null;
            case 235 : // 0xeb
                return null;
            case 236 : // 0xec
                return null;
            case 237 : // 0xed
                return null;
            case 238 : // 0xee
                return null;
            case 239 : // 0xef
                return null;
            case 240 : // 0xf0
                return null;
            case 241 : // 0xf1
                return null;
            case 242 : // 0xf2
                return null;
            case 243 : // 0xf3
                return null;
            case 244 : // 0xf4
                return null;
            case 245 : // 0xf5
                return null;
            case 246 : // 0xf6
                return null;
            case 247 : // 0xf7
                return null;
            case 248 : // 0xf8
                return null;
            case 249 : // 0xf9
                return null;
            case 250 : // 0xfa
                return null;
            case 251 : // 0xfb
                return null;
            case 252 : // 0xfc
                return null;
            case 253 : // 0xfd
                return null;
            case 254 : // 0xfe
                return null;
            case 255 : // 0xff
                return null;
            default:
                throw new IllegalStateException("Unexpected value: " + unsignedOpcode);
        }
    }

}
