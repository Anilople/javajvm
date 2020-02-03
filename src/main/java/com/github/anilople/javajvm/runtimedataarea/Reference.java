package com.github.anilople.javajvm.runtimedataarea;

import com.github.anilople.javajvm.runtimedataarea.reference.ArrayReference;
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

    /**
     * self-define reference is "null" or not.
     * only 1 "null" in jvm, i.e the instance of NullReference
     * @see NullReference
     * @param reference
     * @return
     */
    static boolean isNull(Reference reference) {
        if(null == reference) {
            throw new NullPointerException("Why you pass a real null as parameter instead of Reference NULL?");
        }

        if(reference instanceof NullReference && !NULL.equals(reference)) {
            throw new RuntimeException("Why exists multiple instances of NullReference?");
        }

        return NULL.equals(reference);
    }

    /**
     *
     * @see ArrayReference
     * @param reference
     * @return reference is array or not
     */
    static boolean isArray(Reference reference) {
        return reference instanceof ArrayReference;
    }
}
