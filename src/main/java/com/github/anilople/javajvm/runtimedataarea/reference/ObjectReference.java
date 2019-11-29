package com.github.anilople.javajvm.runtimedataarea.reference;

import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.runtimedataarea.LocalVariables;
import com.github.anilople.javajvm.runtimedataarea.Reference;

public class ObjectReference extends LocalVariables implements Reference {

    private JvmClass jvmClass;

    public ObjectReference(JvmClass jvmClass) {
        // an Object in run-time exist own fields (non static fields)
        super(jvmClass.getNonStaticFieldsSize());
        this.jvmClass = jvmClass;
    }

    @Override
    public String toString() {
        return "ObjectReference{" +
                "jvmClass=" + jvmClass +
                ", super=" + super.toString() +
                '}';
    }
}
