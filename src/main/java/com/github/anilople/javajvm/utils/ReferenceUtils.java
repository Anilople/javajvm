package com.github.anilople.javajvm.utils;

import com.github.anilople.javajvm.cachepool.StringPool;
import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.runtimedataarea.reference.BaseTypeArrayReference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectReference;

/**
 * A bridge between real java object and self-define reference
 */
public class ReferenceUtils {

    /**
     * get the string.
     * use pool to save string
     * @param stringClass must be string class
     * @param utf8 string's content
     * @return object reference of string
     */
    public static ObjectReference getStringObjectReference(JvmClass stringClass, String utf8) {
        if(!stringClass.isSameName(String.class)) {
            throw new RuntimeException(stringClass.getName() + " is not " + String.class);
        }
        // exists in pool or not
        if(!StringPool.exists(utf8)) {
            ObjectReference objectReference = new ObjectReference(stringClass);
            BaseTypeArrayReference charArrayReference = new BaseTypeArrayReference(utf8.toCharArray());
            // use hack skill to generate string
            objectReference.setReference(0, charArrayReference);
            StringPool.add(utf8, objectReference);
        }
        // get string from pool
        return StringPool.get(utf8);
    }

}
