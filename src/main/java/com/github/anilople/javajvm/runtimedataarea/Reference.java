package com.github.anilople.javajvm.runtimedataarea;

import com.github.anilople.javajvm.runtimedataarea.reference.NullReference;

/**
 * reference
 */
public interface Reference {

    /**
     * null value of reference
     */
    Reference NULL = NullReference.getInstance();

    static void assertIsNotNull(Reference reference) {
        if(null == reference) {
            throw new NullPointerException("why you push a real null instead of Reference NULL?");
        }
        if(NULL.equals(reference)) {
            throw new NullPointerException();
        }
    }
}
