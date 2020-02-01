package com.github.anilople.javajvm.heap;

import com.github.anilople.javajvm.classfile.MethodInfo;
import com.github.anilople.javajvm.classfile.attributes.AttributeInfo;
import com.github.anilople.javajvm.classfile.attributes.CodeAttribute;
import com.github.anilople.javajvm.constants.AccessFlags;
import com.github.anilople.javajvm.heap.constant.JvmConstantClass;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;

import java.util.List;

public class JvmMethod extends JvmClassMember {

    private short maxStack;

    private short maxLocals;

    private byte[] code;

    private ExceptionTable[] exceptionTables;

    public JvmMethod(JvmClass jvmClass, MethodInfo methodInfo) {
        super(
            jvmClass,
            methodInfo.getAccessFlags(),
            methodInfo.getName(),
            methodInfo.getDescriptor(),
            methodInfo.getAttributes()
        );

        // from code attribute, get this method's
        // maxStack, maxLocals, code
        for(AttributeInfo attributeInfo : methodInfo.getAttributes()) {
            if(attributeInfo instanceof CodeAttribute) {
                CodeAttribute codeAttribute = (CodeAttribute) attributeInfo;
                this.maxStack = codeAttribute.getMaxStack();
                this.maxLocals = codeAttribute.getMaxLocals();
                this.code = codeAttribute.getCode();
                this.exceptionTables = ExceptionTable.resolveExceptionTables(codeAttribute.getExceptionTable(), this);
            }
        }
    }

    /**
     * get methods from methodInfos
     * @param jvmClass
     * @param methodInfos
     * @return
     */
    public static JvmMethod[] generateJvmMethods(JvmClass jvmClass, MethodInfo[] methodInfos) {
        JvmMethod[] jvmMethods = new JvmMethod[methodInfos.length];
        for(int i = 0; i < jvmMethods.length; i++) {
            jvmMethods[i] = new JvmMethod(jvmClass, methodInfos[i]);
        }
        return jvmMethods;
    }

    public static class ExceptionTable {

        private int startPc;

        private int endPc;

        private int handlerPc;

        /**
         * resolve the catch type here,
         * if raw catch type is 0,
         * a null will be here
         * @see CodeAttribute.ExceptionTableEntry
         */
        private JvmClass catchType;

        /**
         * construct from raw data
         * @param exceptionTableEntry raw data in .class file
         * @param catchType exception class resolved
         */
        public ExceptionTable(CodeAttribute.ExceptionTableEntry exceptionTableEntry, JvmClass catchType) {
            this.startPc = exceptionTableEntry.getStartPc();
            this.endPc = exceptionTableEntry.getEndPc();
            this.handlerPc = exceptionTableEntry.getHandlerPc();
            this.catchType = catchType;
        }

        /**
         * exception_table  belong to Code_attribute
         * Code_attribute   belong to a java method
         * @param exceptionTableEntries raw data in .class file
         * @param jvmMethod which method those exception tables belong to
         * @return exception tables resolved
         */
        public static ExceptionTable[] resolveExceptionTables(
                CodeAttribute.ExceptionTableEntry[] exceptionTableEntries,
                JvmMethod jvmMethod) {
            final JvmConstantPool jvmConstantPool = jvmMethod.getJvmClass().getJvmConstantPool();
            final int length = exceptionTableEntries.length;
            ExceptionTable[] exceptionTables = new ExceptionTable[length];
            for(int i = 0; i < length; i++) {
                final CodeAttribute.ExceptionTableEntry exceptionTableEntry = exceptionTableEntries[i];
                final short catchType = exceptionTableEntry.getCatchType();
                if(0 == catchType) {
                    // catch all, use to implement finally
                    exceptionTables[i] = new ExceptionTable(exceptionTableEntry, null);
                } else {
                    // catch one exception
                    JvmConstantClass jvmConstantClass = (JvmConstantClass) jvmConstantPool.getJvmConstant(PrimitiveTypeUtils.intFormUnsignedShort(catchType));
                    exceptionTables[i] = new ExceptionTable(exceptionTableEntry, jvmConstantClass.resolveJvmClass());
                }
            }
            return exceptionTables;
        }

        public int getStartPc() {
            return startPc;
        }

        public int getEndPc() {
            return endPc;
        }

        public int getHandlerPc() {
            return handlerPc;
        }

        public JvmClass getCatchType() {
            return catchType;
        }
    }

    public boolean isPublic() {
        return 0 != (this.getAccessFlags() & AccessFlags.MethodFlags.ACC_PUBLIC);
    }

    public boolean isPrivate() {
        return 0 != (this.getAccessFlags() & AccessFlags.MethodFlags.ACC_PRIVATE);
    }

    public boolean isProtected() {
        return 0 != (this.getAccessFlags() & AccessFlags.MethodFlags.ACC_PROTECTED);
    }

    public boolean isStatic() {
        return 0 != (this.getAccessFlags() & AccessFlags.MethodFlags.ACC_STATIC);
    }

    public boolean isFinal() {
        return 0 != (this.getAccessFlags() & AccessFlags.MethodFlags.ACC_FINAL);
    }

    public boolean isSynchronized() {
        return 0 != (this.getAccessFlags() & AccessFlags.MethodFlags.ACC_SYNCHRONIZED);
    }

    public boolean isBridge() {
        return 0 != (this.getAccessFlags() & AccessFlags.MethodFlags.ACC_BRIDGE);
    }

    public boolean isVarargs() {
        return 0 != (this.getAccessFlags() & AccessFlags.MethodFlags.ACC_VARARGS);
    }

    public boolean isNative() {
        return 0 != (this.getAccessFlags() & AccessFlags.MethodFlags.ACC_NATIVE);
    }

    public boolean isAbstract() {
        return 0 != (this.getAccessFlags() & AccessFlags.MethodFlags.ACC_ABSTRACT);
    }

    public boolean isStrictfp() {
        return 0 != (this.getAccessFlags() & AccessFlags.MethodFlags.ACC_STRICT);
    }

    public boolean isSynthetic() {
        return 0 != (this.getAccessFlags() & AccessFlags.MethodFlags.ACC_SYNTHETIC);
    }

    public short getMaxStack() {
        return maxStack;
    }

    public short getMaxLocals() {
        return maxLocals;
    }

    public byte[] getCode() {
        // return a clone to forbidden changing code
        return code.clone();
    }
}
