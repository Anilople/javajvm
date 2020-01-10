package com.github.anilople.javajvm.utils;

import com.github.anilople.javajvm.heap.JvmClassLoader;
import com.github.anilople.javajvm.helper.JvmClassLoaderFactory;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.BaseTypeArrayReference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectArrayReference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectReference;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReferenceUtilsTest {

    private static final JvmClassLoader jvmClassLoader = JvmClassLoaderFactory.getInstance();

    @Test
    void getStringObjectReference() {
        final String utf8 = "abcd";
        ObjectReference objectReference = ReferenceUtils.getStringObjectReference(
                jvmClassLoader.loadClass(String.class),
                utf8
        );
        BaseTypeArrayReference baseTypeArrayReference = (BaseTypeArrayReference) objectReference.getReference(0);
        assertEquals(utf8.length(), baseTypeArrayReference.length());
        for(int i = 0; i < utf8.length(); i++) {
            assertEquals(utf8.charAt(i), baseTypeArrayReference.getCharValue(i));
        }
    }

    @Test
    void singleDimensionBooleanArray2ArrayReference() throws IllegalAccessException {
        boolean[] booleans = new boolean[]{false, true, false};
        BaseTypeArrayReference booleanArray = (BaseTypeArrayReference) ReferenceUtils.array2ArrayReference(jvmClassLoader, booleans);
        for(int i = 0; i < booleans.length; i++) {
            assertEquals(booleans[i], booleanArray.getBooleanValue(i));
        }
    }

    @Test
    void singleDimensionCharArray2ArrayReference() throws IllegalAccessException {
        char[] chars = new char[]{'a', '1', '+'};
        BaseTypeArrayReference charArray = (BaseTypeArrayReference) ReferenceUtils.array2ArrayReference(jvmClassLoader, chars);
        for(int i = 0; i < chars.length; i++) {
            assertEquals(chars[i], charArray.getCharValue(i));
        }
    }

    @Test
    void singleDimensionIntArray2ArrayReference() throws IllegalAccessException {
        int[] ints = new int[]{-1, 2, 3, 5};
        BaseTypeArrayReference intArray = (BaseTypeArrayReference) ReferenceUtils.array2ArrayReference(jvmClassLoader, ints);
        for(int i = 0; i < ints.length; i++) {
            assertEquals(ints[i], intArray.getIntValue(i));
        }
    }

    @Test
    void singleDimensionObjectArray2ObjectArrayReference() throws IllegalAccessException {
        Object[] objects = new Object[]{"sdf", "hei"};
        ObjectArrayReference objectArrayReference = ReferenceUtils.singleDimensionObjectArray2ObjectArrayReference(jvmClassLoader, objects);
        assertEquals(objects.length, objectArrayReference.length());
        ObjectReference a = (ObjectReference) objectArrayReference.getReference(0);
        BaseTypeArrayReference aCharArray = (BaseTypeArrayReference) a.getReference(0);
        assertEquals('d', aCharArray.getCharValue(1));

        ObjectReference b = (ObjectReference) objectArrayReference.getReference(1);
        BaseTypeArrayReference bCharArray = (BaseTypeArrayReference) b.getReference(0);
        assertEquals('h', bCharArray.getCharValue(0));
    }

    @Test
    void null2Reference() throws IllegalAccessException {
        Reference reference = ReferenceUtils.object2Reference(jvmClassLoader, null);
        assertEquals(reference, Reference.NULL);
    }

    @Test
    void string2Reference() throws IllegalAccessException {
        final String s = "abc";
        Reference reference = ReferenceUtils.object2Reference(jvmClassLoader, s);
        ObjectReference objectReference = (ObjectReference) reference;
        assertTrue(objectReference.getJvmClass().isSameName(String.class));
        // first field in string is char[], now get it
        BaseTypeArrayReference charArray = (BaseTypeArrayReference) objectReference.getReference(0);
        // check char values in char[]
        for(int i = 0; i < s.length(); i++) {
            assertEquals(s.charAt(i), charArray.getCharValue(i));
        }
    }
}