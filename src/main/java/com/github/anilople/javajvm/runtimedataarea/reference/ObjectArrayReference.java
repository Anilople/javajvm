package com.github.anilople.javajvm.runtimedataarea.reference;

import com.github.anilople.javajvm.runtimedataarea.Reference;

import java.util.Arrays;

public class ObjectArrayReference extends ArrayReference {

    private Reference typeReference;

    /**
     * reference
     */
    private Reference[] references;


    public ObjectArrayReference(Reference typeReference, int count) {
        super(count);
        this.typeReference = typeReference;
        references = new Reference[count];
        Arrays.fill(references, Reference.NULL);
    }

    public Reference getTypeReference() {
        return typeReference;
    }

    @Override
    public String toString() {
        return "ObjectArrayReference{" +
                "typeReference=" + typeReference +
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
