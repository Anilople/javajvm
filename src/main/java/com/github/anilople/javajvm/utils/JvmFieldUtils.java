package com.github.anilople.javajvm.utils;

import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.heap.JvmField;

/**
 * tools for JvmField
 */
public class JvmFieldUtils {

    /**
     * calculate that the offset of a non static field in a class
     * just remember that long and double occupy 2 location
     * @param jvmClass
     * @param jvmField a non static field
     * @return
     */
    public static int calculateNonStaticFieldOffset(JvmClass jvmClass, JvmField jvmField) {
        int offset = 0;
        for(JvmClass tempClass : JvmClassUtils.getInheritedClassesChain(jvmClass)) {
            offset += tempClass.getNonStaticFieldsSize();
        }

        // subtract now class fields's size
        offset -= jvmClass.getNonStaticFieldsSize();

        // add offset
        for(JvmField tempField : jvmClass.getJvmFields()) {
            if(tempField.equals(jvmField)) {
                // meet same
                break;
            }

            if(!tempField.isStatic()) {
                offset += tempField.getSize();
            }
        }

        return offset;
    }

}
