package com.github.anilople.javajvm.runtimedataarea.reference;

import com.github.anilople.javajvm.runtimedataarea.Reference;

import java.util.Arrays;

public class ObjectArrayReference extends ArrayReference {

    /**
     * array exists component,
     * this field represents the type of component
     */
    private Reference componentTypeReference;

    /**
     * reference
     */
    private Reference[] references;


    public ObjectArrayReference(Reference componentTypeReference, int count) {
        super(count);
        this.componentTypeReference = componentTypeReference;
        references = new Reference[count];
        Arrays.fill(references, Reference.NULL);
    }

    public Reference getComponentTypeReference() {
        return componentTypeReference;
    }

    @Override
    public String toString() {
        return "ObjectArrayReference{" +
                "typeReference=" + componentTypeReference +
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
