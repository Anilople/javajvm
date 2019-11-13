package com.github.anilople.javajvm.runtimedataarea;

import com.github.anilople.javajvm.runtimedataarea.reference.NullReference;

/**
 * reference
 */
public interface Reference {

    /**
     * null value of reference
     */
    Reference NULL = new NullReference();
}
