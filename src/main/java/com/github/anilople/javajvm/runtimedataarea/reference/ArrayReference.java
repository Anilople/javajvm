package com.github.anilople.javajvm.runtimedataarea.reference;

import com.github.anilople.javajvm.constants.ArrayTypeCodes;
import com.github.anilople.javajvm.runtimedataarea.Reference;

/**
 * array reference
 *
 */
public abstract class ArrayReference implements Reference {

    /**
     * array length
     */
    private final int count;

    public ArrayReference(int count) {
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

}
