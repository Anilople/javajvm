package com.github.anilople.javajvm.runtimedataarea.reference;

import com.github.anilople.javajvm.runtimedataarea.Reference;

/**
 * null reference
 * Singleton mode
 */
public class NullReference implements Reference {

    public static final NullReference nullReference = new NullReference();

    private NullReference() {

    }

    public static NullReference getInstance() {
        return nullReference;
    }

}
