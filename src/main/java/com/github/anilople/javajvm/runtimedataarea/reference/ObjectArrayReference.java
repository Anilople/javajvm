package com.github.anilople.javajvm.runtimedataarea.reference;

import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.runtimedataarea.Reference;

import java.util.Arrays;

public class ObjectArrayReference extends ArrayReference {

    /**
     * array exists component,
     * this field represents the type of component
     */
    private JvmClass componentType;

    /**
     * reference
     */
    private Reference[] references;


    public ObjectArrayReference(JvmClass componentType, int count) {
        super(count);
        this.componentType = componentType;
        references = new Reference[count];
        Arrays.fill(references, Reference.NULL);
    }

    public JvmClass getComponentType() {
        return componentType;
    }

    @Override
    public String toString() {
        return "ObjectArrayReference{" +
                "typeReference=" + componentType +
                ", references=" + Arrays.toString(references) +
                '}';
    }

    public void setReference(int index, Reference reference) {
        references[index] = reference;
    }

    public Reference getReference(int index) {
        return references[index];
    }
}
