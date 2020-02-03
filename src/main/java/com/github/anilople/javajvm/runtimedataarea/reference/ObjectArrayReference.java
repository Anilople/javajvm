package com.github.anilople.javajvm.runtimedataarea.reference;

import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.runtimedataarea.Reference;

import java.util.Arrays;

public class ObjectArrayReference extends ArrayReference {

    /**
     * reference
     */
    private Reference[] references;


    public ObjectArrayReference(JvmClass componentType, int count) {
        super(componentType, count);
        references = new Reference[count];
        Arrays.fill(references, Reference.NULL);
    }

    @Override
    public String toString() {
        return "ObjectArrayReference{" +
                "componentType=" + this.getComponentType() +
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
