package com.github.anilople.javajvm.runtimedataarea.reference;

import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.runtimedataarea.Reference;

public class ObjectReference implements Reference {

    private JvmClass jvmClass;



    private ObjectReference() {

    }

    public ObjectReference(JvmClass jvmClass) {
        this.jvmClass = jvmClass;
    }

}
