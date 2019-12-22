package com.github.anilople.javajvm.heap;

import com.github.anilople.javajvm.classfile.MethodInfo;
import com.github.anilople.javajvm.classfile.attributes.AttributeInfo;
import com.github.anilople.javajvm.classfile.attributes.CodeAttribute;
import com.github.anilople.javajvm.constants.AccessFlags;

import java.util.List;

public class JvmMethod extends JvmClassMember {

    private short maxStack;

    private short maxLocals;

    private byte[] code;

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
