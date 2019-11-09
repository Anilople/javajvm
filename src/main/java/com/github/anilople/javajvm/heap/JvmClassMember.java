package com.github.anilople.javajvm.heap;

import com.github.anilople.javajvm.classfile.attributes.AttributeInfo;
import com.github.anilople.javajvm.constants.AccessFlags;

/**
 * abstract field and method in this class
 * there are same things between field and method
 */
public abstract class JvmClassMember {

    private JvmClass jvmClass;

    private short accessFlags;

    private String name;

    private String descriptor;

    private AttributeInfo[] attributes;

    private JvmClassMember() {

    }

    public JvmClassMember(JvmClass jvmClass, short accessFlags, String name, String descriptor, AttributeInfo[] attributes) {
        this.jvmClass = jvmClass;
        this.accessFlags = accessFlags;
        this.name = name;
        this.descriptor = descriptor;
        this.attributes = attributes;
    }

    public boolean isPublic() {
        return 0 != (this.accessFlags & AccessFlags.ACC_PUBLIC);
    }

    public boolean isPrivate() {
        return 0 != (this.accessFlags & AccessFlags.ACC_PRIVATE);
    }

    public boolean isProtected() {
        return 0 != (this.accessFlags & AccessFlags.ACC_PROTECTED);
    }

    public boolean isStatic() {
        return 0 != (this.accessFlags & AccessFlags.ACC_STATIC);
    }

    public boolean isFinal() {
        return 0 != (this.accessFlags & AccessFlags.ACC_FINAL);
    }

    public short getAccessFlags() {
        return accessFlags;
    }

    public String getName() {
        return name;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public JvmClass getJvmClass() {
        return jvmClass;
    }
}
