package com.github.anilople.javajvm.runtimedataarea.reference;

import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.runtimedataarea.Reference;

/**
 * array reference
 *
 */
public abstract class ArrayReference implements Reference {

    /**
     * array exists component,
     * this field represents the type of component
     */
    private JvmClass componentType;

    /**
     * array length
     */
    private final int count;

    public ArrayReference(JvmClass componentType, int count) {
        this.componentType = componentType;
        this.count = count;
    }

    /**
     * @return array's length
     */
    public int length() {
        return count;
    }

    public void assertIndexIsNotOutOfBounds(int index) {
        if(index < 0 || index >= count) {
            throw new ArrayIndexOutOfBoundsException("" + index);
        }
    }

    public JvmClass getComponentType() {
        return componentType;
    }
}
