package com.github.anilople.javajvm.runtimedataarea.reference;

import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.heap.JvmClassLoader;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.utils.ReflectionUtils;

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

    /**
     * We know the component type of this array,
     * but what is its type?
     * Example:
     *      type        component type
     *      Object[]    Object
     *      int[][]     int[]
     * Can we get its type byte its component type?
     * @return this array's type
     */
    public JvmClass resolveType() {
        return this.getComponentType().wrapperArray();
    }
}
