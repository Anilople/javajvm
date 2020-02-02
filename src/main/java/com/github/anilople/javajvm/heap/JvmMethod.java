package com.github.anilople.javajvm.heap;

import com.github.anilople.javajvm.classfile.MethodInfo;
import com.github.anilople.javajvm.classfile.attributes.AttributeInfo;
import com.github.anilople.javajvm.classfile.attributes.CodeAttribute;
import com.github.anilople.javajvm.constants.AccessFlags;
import com.github.anilople.javajvm.heap.constant.JvmConstantClass;
import com.github.anilople.javajvm.utils.PrimitiveTypeUtils;

import java.util.Objects;

public class JvmMethod extends JvmClassMember {

    private short maxStack;

    private short maxLocals;

    private byte[] code;

    private ExceptionHandler[] exceptionHandlers;

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
                this.exceptionHandlers = ExceptionHandler.generateExceptionTables(this, (codeAttribute.getExceptionTable()));
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

    /**
     *
     * @param exceptionClass
     * @return exists exception handler of exception class given in this method or not
     */
    public boolean existsExceptionHandler(JvmClass exceptionClass) {
        for(ExceptionHandler exceptionHandler : exceptionHandlers) {
            if(exceptionHandler.matchExceptionClass(exceptionClass)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param exceptionClass exception class
     * @throws RuntimeException if there are not exception handler match
     * @return exception handler
     */
    public ExceptionHandler getExceptionHandler(JvmClass exceptionClass) {
        for(ExceptionHandler exceptionHandler : exceptionHandlers) {
            if(exceptionHandler.matchExceptionClass(exceptionClass)) {
                return exceptionHandler;
            }
        }
        throw new RuntimeException("No exception handler for exception " + exceptionClass);
    }

    public static class ExceptionHandler {

        private JvmMethod jvmMethod;

        private CodeAttribute.ExceptionTableEntry exceptionTableEntry;

        /**
         * construct from raw data
         * @param jvmMethod which method this exception table belong to
         * @param exceptionTableEntry raw data in .class file
         */
        public ExceptionHandler(JvmMethod jvmMethod, CodeAttribute.ExceptionTableEntry exceptionTableEntry) {
            this.jvmMethod = jvmMethod;
            this.exceptionTableEntry = exceptionTableEntry;
        }

        /**
         * exception_table  belong to Code_attribute
         * Code_attribute   belong to a java method
         * @param jvmMethod which method those exception tables belong to
         * @param exceptionTableEntries raw data in .class file
         * @return exception tables resolved
         */
        public static ExceptionHandler[] generateExceptionTables(
                JvmMethod jvmMethod,
            CodeAttribute.ExceptionTableEntry[] exceptionTableEntries) {
            final JvmConstantPool jvmConstantPool = jvmMethod.getJvmClass().getJvmConstantPool();
            final int length = exceptionTableEntries.length;
            ExceptionHandler[] exceptionHandlers = new ExceptionHandler[length];
            for(int i = 0; i < length; i++) {
                final CodeAttribute.ExceptionTableEntry exceptionTableEntry = exceptionTableEntries[i];
                exceptionHandlers[i] = new ExceptionHandler(jvmMethod, exceptionTableEntry);
            }
            return exceptionHandlers;
        }

        /**
         *
         * @param exceptionClass
         * @return this exception handler can handle the exception given or not
         */
        public boolean matchExceptionClass(JvmClass exceptionClass) {
            Objects.requireNonNull(exceptionClass, "You can not pass a null value with class");
            JvmClass catchType = this.resolveCatchType();
            if(null == catchType) {
                // match all, "finally" in Java
                return true;
            } else {
                return exceptionClass.equals(catchType) || exceptionClass.isSubClassOf(catchType);
            }
        }

        /**
         * resolve the catch type here,
         * if raw catch type is 0,
         * a null will be here
         * @see CodeAttribute.ExceptionTableEntry
         */
        public JvmClass resolveCatchType() {
            short catchTypeIndex = exceptionTableEntry.getCatchType();
            if(0 == catchTypeIndex) {
                // catch all, use to implement finally
                return null;
            } else {
                // catch one exception
                // cache the resolve type here, todo
                JvmConstantPool jvmConstantPool = jvmMethod.getJvmClass().getJvmConstantPool();
                JvmConstantClass jvmConstantClass = (JvmConstantClass) jvmConstantPool.getJvmConstant(PrimitiveTypeUtils.intFormUnsignedShort(catchTypeIndex));
                return jvmConstantClass.resolveJvmClass();
            }
        }

        public int getStartPc() {
            return exceptionTableEntry.getStartPc();
        }

        public int getEndPc() {
            return exceptionTableEntry.getEndPc();
        }

        public int getHandlerPc() {
            return exceptionTableEntry.getHandlerPc();
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
