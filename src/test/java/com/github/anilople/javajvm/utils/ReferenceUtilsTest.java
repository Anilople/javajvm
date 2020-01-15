package com.github.anilople.javajvm.utils;

import com.github.anilople.javajvm.heap.JvmClassLoader;
import com.github.anilople.javajvm.helper.JvmClassLoaderFactory;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.BaseTypeArrayReference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectArrayReference;
import com.github.anilople.javajvm.runtimedataarea.reference.ObjectReference;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

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
        BaseTypeArrayReference booleanArray = (BaseTypeArrayReference) ReferenceUtils.array2ArrayReference(new HashMap<>(), jvmClassLoader, booleans);
        for(int i = 0; i < booleans.length; i++) {
            assertEquals(booleans[i], booleanArray.getBooleanValue(i));
        }
    }

    @Test
    void singleDimensionCharArray2ArrayReference() throws IllegalAccessException {
        char[] chars = new char[]{'a', '1', '+'};
        BaseTypeArrayReference charArray = (BaseTypeArrayReference) ReferenceUtils.array2ArrayReference(new HashMap<>(), jvmClassLoader, chars);
        for(int i = 0; i < chars.length; i++) {
            assertEquals(chars[i], charArray.getCharValue(i));
        }
    }

    @Test
    void singleDimensionIntArray2ArrayReference() throws IllegalAccessException {
        int[] ints = new int[]{-1, 2, 3, 5};
        BaseTypeArrayReference intArray = (BaseTypeArrayReference) ReferenceUtils.array2ArrayReference(new HashMap<>(), jvmClassLoader, ints);
        for(int i = 0; i < ints.length; i++) {
            assertEquals(ints[i], intArray.getIntValue(i));
        }
    }

    @Test
    void singleDimensionObjectArray2ObjectArrayReference() throws IllegalAccessException {
        Object[] objects = new Object[]{"sdf", "hei"};
        ObjectArrayReference objectArrayReference = ReferenceUtils.singleDimensionObjectArray2ObjectArrayReference(new HashMap<>(), jvmClassLoader, objects);
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

    @Test
    void baseTypeArrayReference2Object_Char() {
        char[] chars = new char[]{'a', '1', '+'};
        BaseTypeArrayReference baseTypeArrayReference = ReferenceUtils.singleDimensionPrimitiveArray2ArrayReference(chars);
        char[] charsImage = (char[]) ReferenceUtils.baseTypeArrayReference2Object(baseTypeArrayReference);
        assertEquals(chars.length, charsImage.length);
        for(int i = 0; i < chars.length; i++) {
            assertEquals(chars[i], charsImage[i]);
        }
    }

    @Test
    void baseTypeArrayReference2Object_Double() {
        double[] doubles = new double[]{1.0D, 2.999999D, 6.666666D};
        BaseTypeArrayReference baseTypeArrayReference = ReferenceUtils.singleDimensionPrimitiveArray2ArrayReference(doubles);
        double[] doublesImage = (double[]) ReferenceUtils.baseTypeArrayReference2Object(baseTypeArrayReference);
        assertEquals(doubles.length, doublesImage.length);
        for(int i = 0; i < doubles.length; i++) {
            assertEquals(doubles[i], doublesImage[i]);
        }
    }

    @Test
    void objectReference2Object() throws IllegalAccessException {

        Values values = new Values();

        ObjectReference objectReference = (ObjectReference) ReferenceUtils.object2ObjectReference(jvmClassLoader, values);

        Values valuesImage = (Values) ReferenceUtils.objectReference2Object(objectReference);

        values.equals(valuesImage);

        assertEquals(values.getIntValue(), valuesImage.getIntValue());
        assertEquals(values.getLongValue(), valuesImage.getLongValue());
        assertEquals(values.getDoubleValue(), valuesImage.getDoubleValue());
        char[] valuesCharArray = values.getChars();
        char[] valuesImageCharArray = valuesImage.getChars();
        assertEquals(valuesCharArray.length, valuesImageCharArray.length);
        for(int i = 0; i < valuesCharArray.length; i++) {
            assertEquals(valuesCharArray[i], valuesImageCharArray[i]);
        }
    }
}

/**
 * for the field test
 * the inner class will exists 'this$0' field
 * so declare it here
 */
class Values {
    private int intValue = 3;
    private long longValue = 666L;
    private char[] chars = new char[]{'1', '+'};
    private double doubleValue = 8.888D;

    public int getIntValue() {
        return intValue;
    }

    public long getLongValue() {
        return longValue;
    }

    public char[] getChars() {
        return chars;
    }

    public double getDoubleValue() {
        return doubleValue;
    }
}
