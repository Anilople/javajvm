package com.github.anilople.javajvm.utils;

import com.github.anilople.javajvm.cachepool.StringPool;
import com.github.anilople.javajvm.constants.ArrayTypeCodes;
import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.heap.JvmClassLoader;
import com.github.anilople.javajvm.runtimedataarea.LocalVariables;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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


    /**
     * convert a real object in JVM to
     * self-define reference
     * @param jvmClassLoader
     * @param object
     * @return
     */
    public static Reference object2Reference(JvmClassLoader jvmClassLoader, Object object) throws IllegalAccessException {
        if(null == object) {
            return Reference.NULL;
        }
        final Class<?> clazz = object.getClass();
        // array
        if(clazz.isArray()) {
            return array2ArrayReference(jvmClassLoader, object);
        }

        // object

        // get the JvmClass of object
        JvmClass jvmClass = jvmClassLoader.loadClass(clazz);
        ObjectReference objectReference = new ObjectReference(jvmClass);

        // converter the static fields, todo

        // converter the non-static fields
        List<Field> nonStaticFields = getNonStaticFieldsFromAncestor(clazz);
        int nonStaticFieldCurrentOffset = 0;
        for(Field nonStaticField : nonStaticFields) {
            if(nonStaticField.getType().isPrimitive()) {
                setPrimitiveType(objectReference, nonStaticFieldCurrentOffset, object, nonStaticField);
            } else {
                setReferenceType(objectReference, nonStaticFieldCurrentOffset, jvmClassLoader, object, nonStaticField);
            }
            nonStaticFieldCurrentOffset += getFieldSize(nonStaticField);
        }

        return objectReference;
    }

    /**
     * convert an array object to self-define array reference
     * @param arrayObject array
     * @return array reference
     */
    public static ArrayReference array2ArrayReference(
            JvmClassLoader jvmClassLoader, Object arrayObject
    ) throws IllegalAccessException {
        int dimensions = DescriptorUtils.getDimensions(arrayObject.getClass().getName());
        if(dimensions <= 0) {
            throw new RuntimeException(arrayObject + " is not array");
        }
        if(1 == dimensions) {
            // end the recursion
            final Class<?> componentType = arrayObject.getClass().getComponentType();
            if(componentType.isPrimitive()) {
                return singleDimensionPrimitiveArray2ArrayReference(arrayObject);
            } else {
                return singleDimensionObjectArray2ObjectArrayReference(jvmClassLoader, arrayObject);
            }
        }

        // multiple dimensions
        throw new RuntimeException("Now cannot support multiple dimensions array");
    }

    /**
     * @param primitiveArrayObject 1 dimension array of primitive type
     * @return self-define array reference
     */
    public static BaseTypeArrayReference singleDimensionPrimitiveArray2ArrayReference(Object primitiveArrayObject) {
        // get component type
        final Class<?> type = primitiveArrayObject.getClass().getComponentType();
        if(type.equals(boolean.class)) {
            return new BaseTypeArrayReference((boolean[]) primitiveArrayObject);
        } else if(type.equals(byte.class)) {
            return new BaseTypeArrayReference((byte[]) primitiveArrayObject);
        } else if(type.equals(short.class)) {
            return new BaseTypeArrayReference((short[]) primitiveArrayObject);
        } else if(type.equals(char.class)) {
            return new BaseTypeArrayReference((char[]) primitiveArrayObject);
        } else if(type.equals(int.class)) {
            return new BaseTypeArrayReference((int[]) primitiveArrayObject);
        } else if(type.equals(float.class)) {
            return new BaseTypeArrayReference((float[]) primitiveArrayObject);
        } else if(type.equals(long.class)) {
            return new BaseTypeArrayReference((long[]) primitiveArrayObject);
        } else if(type.equals(double.class)) {
            return new BaseTypeArrayReference((double[]) primitiveArrayObject);
        } else {
            throw new IllegalArgumentException("Cannot set type " + type);
        }
    }

    /**
     * @param objectArrayObject 1 dimension array of object reference
     * @return self-define array reference
     */
    public static ObjectArrayReference singleDimensionObjectArray2ObjectArrayReference(
            JvmClassLoader jvmClassLoader, Object objectArrayObject
    ) throws IllegalAccessException {
        Object[] objects = (Object[]) objectArrayObject;
        Class<?> componentClass = objects.getClass().getComponentType();
        ObjectArrayReference objectArrayReference = new ObjectArrayReference(
                jvmClassLoader.loadClass(componentClass),
                objects.length
        );
        for(int i = 0; i < objects.length; i++) {
            Reference reference = object2Reference(jvmClassLoader, objects[i]);
            objectArrayReference.setReference(i, reference);
        }
        return objectArrayReference;
    }

    /**
     * get all field from ancestor to current class
     * keep the order
     * @param clazz current class
     * @return
     */
    private static List<Field> getAllFieldsFromAncestor(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        for(Class<?> now = clazz; null != now; now = now.getSuperclass()) {
            Field[] nowFields = now.getDeclaredFields();
            // add from last to head
            for(int i = nowFields.length - 1; i >= 0; i--) {
                fields.add(nowFields[i]);
            }
        }
        // remember to reverse the fields got
        Collections.reverse(fields);
        return fields;
    }

    /**
     * get all non static fields of a class (from ancestor to current)
     * keep the order
     * @param clazz
     * @return
     */
    private static List<Field> getNonStaticFieldsFromAncestor(Class<?> clazz) {
        List<Field> nonStaticFields = new ArrayList<>();
        List<Field> allFields = getAllFieldsFromAncestor(clazz);
        for(Field field : allFields) {
            if(!Modifier.isStatic(field.getModifiers())) {
                nonStaticFields.add(field);
            }
        }
        return nonStaticFields;
    }

    /**
     * get all static fields of a class (from ancestor to current)
     * keep the order
     * @param clazz
     * @return
     */
    private static List<Field> getStaticFieldsFromAncestor(Class<?> clazz) {
        List<Field> nonStaticFields = new ArrayList<>();
        List<Field> allFields = getAllFieldsFromAncestor(clazz);
        for(Field field : allFields) {
            if(Modifier.isStatic(field.getModifiers())) {
                nonStaticFields.add(field);
            }
        }
        return nonStaticFields;
    }

    /**
     * the field's size in local variables and operand stack
     * long and double will occupy 2 size
     * other is 1
     * @param field
     * @return
     */
    private static int getFieldSize(Field field) {
        Class<?> fieldType = field.getType();
        if(fieldType.equals(long.class)
        || fieldType.equals(double.class)) {
            return 2;
        } else {
            return 1;
        }
    }

    /**
     * set the field's value to position offset
     * long and double will occupy 2 locations
     * @param localVariables super class of ObjectReference
     * @param offset position in the object
     * @param field must be primitive type
     */
    private static void setPrimitiveType(LocalVariables localVariables, int offset, Object object, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        final Class<?> fieldType = field.getType();
        final Class<?> type = fieldType;
        if(type.equals(boolean.class)) {
            boolean value = field.getBoolean(object);
            localVariables.setBooleanValue(offset, value);
        } else if(type.equals(byte.class)) {
            byte value = field.getByte(object);
            localVariables.setByteValue(offset, value);
        } else if(type.equals(short.class)) {
            short value = field.getShort(object);
            localVariables.setShortValue(offset, value);
        } else if(type.equals(char.class)) {
            char value = field.getChar(object);
            localVariables.setCharValue(offset, value);
        } else if(type.equals(int.class)) {
            int value = field.getInt(object);
            localVariables.setIntValue(offset, value);
        } else if(type.equals(float.class)) {
            float value = field.getFloat(object);
            localVariables.setFloatValue(offset, value);
        } else if(type.equals(long.class)) {
            long value = field.getLong(object);
            localVariables.setLongValue(offset, value);
        } else if(type.equals(double.class)) {
            double value = field.getDouble(object);
            localVariables.setDoubleValue(offset, value);
        } else {
            throw new IllegalArgumentException("Cannot set type " + type);
        }
    }


    /**
     * set the value to position offset
     * according to the value's type
     * the value must can unpack to primitive value
     * @param type value's type
     * @param baseTypeArrayReference
     * @param offset
     * @param value primitive type value
     */
    private static void setPrimitiveType(
            Class<?> type,
            BaseTypeArrayReference baseTypeArrayReference, int offset,
            Object value) {
        if(type.equals(boolean.class)) {
            baseTypeArrayReference.setBooleanValue(offset, (Boolean) value);
        } else if(type.equals(byte.class)) {
            baseTypeArrayReference.setByteValue(offset, (Byte) value);
        } else if(type.equals(short.class)) {
            baseTypeArrayReference.setShortValue(offset, (Short) value);
        } else if(type.equals(char.class)) {
            baseTypeArrayReference.setCharValue(offset, (Character) value);
        } else if(type.equals(int.class)) {
            baseTypeArrayReference.setIntValue(offset, (Integer) value);
        } else if(type.equals(float.class)) {
            baseTypeArrayReference.setFloatValue(offset, (Float) value);
        } else if(type.equals(long.class)) {
            baseTypeArrayReference.setLongValue(offset, (Long) value);
        } else if(type.equals(double.class)) {
            baseTypeArrayReference.setDoubleValue(offset, (Double) value);
        } else {
            throw new IllegalArgumentException("Cannot set type " + type);
        }
    }


    /**
     * set the value to position offset
     * according to the value's type
     * the value must be object reference
     * @param localVariables
     * @param fieldCurrentOffset
     * @param jvmClassLoader
     * @param object
     * @param field
     */
    private static void setReferenceType(
            LocalVariables localVariables, int fieldCurrentOffset,
            JvmClassLoader jvmClassLoader,
            Object object, Field field
    ) throws IllegalAccessException {
        // get the field
        field.setAccessible(true);
        Object fieldObject = field.get(object);
        // resolve the reference
        Reference reference = object2Reference(jvmClassLoader, fieldObject);
        // set the reference
        localVariables.setReference(fieldCurrentOffset, reference);
    }

    /**
     * convert self-define reference to a real object
     * @param reference
     * @return
     */
    public static Object reference2Object(Reference reference) {
        if(reference instanceof NullReference) {
            return null;
        } else if(reference instanceof ObjectReference) {
            return objectReference2Object((ObjectReference) reference);
        } else if(reference instanceof ArrayReference) {
            return arrayReference2Object((ArrayReference) reference);
        } else {
            throw new IllegalStateException("Unexpected value: " + reference.toString());
        }
    }

    private static Object objectReference2Object(ObjectReference objectReference) {
        return null;
    }

    private static Object arrayReference2Object(ArrayReference arrayReference) {
        if(arrayReference instanceof BaseTypeArrayReference) {
            return baseTypeArrayReference2Object((BaseTypeArrayReference) arrayReference);
        } else if(arrayReference instanceof ObjectArrayReference) {
            return objectArrayReference2Object((ObjectArrayReference) arrayReference);
        } else {
            throw new IllegalStateException("Unexpected value: " + arrayReference.toString());
        }
    }

    public static Object baseTypeArrayReference2Object(BaseTypeArrayReference baseTypeArrayReference) {
        final int length = baseTypeArrayReference.length();
        final byte typeCode = baseTypeArrayReference.getTypeCode();
        switch (typeCode) {
            case ArrayTypeCodes.T_BOOLEAN:
                boolean[] booleans = new boolean[length];
                for(int i = 0; i < length; i++) {
                    booleans[i] = baseTypeArrayReference.getBooleanValue(i);
                }
                return booleans;
            case ArrayTypeCodes.T_BYTE:
                byte[] bytes = new byte[length];
                for(int i = 0; i < length; i++) {
                    bytes[i] = baseTypeArrayReference.getByteValue(i);
                }
                return bytes;
            case ArrayTypeCodes.T_CHAR:
                char[] chars = new char[length];
                for(int i = 0; i < length; i++) {
                    chars[i] = baseTypeArrayReference.getCharValue(i);
                }
                return chars;
            case ArrayTypeCodes.T_DOUBLE:
                double[] doubles = new double[length];
                for(int i = 0; i < length; i++) {
                    doubles[i] = baseTypeArrayReference.getDoubleValue(i);
                }
                return doubles;
            case ArrayTypeCodes.T_FLOAT:
                float[] floats = new float[length];
                for(int i = 0; i < length; i++) {
                    floats[i] = baseTypeArrayReference.getFloatValue(i);
                }
                return floats;
            case ArrayTypeCodes.T_INT:
                int[] ints = new int[length];
                for(int i = 0; i < length; i++) {
                    ints[i] = baseTypeArrayReference.getIntValue(i);
                }
                return ints;
            case ArrayTypeCodes.T_LONG:
                long[] longs = new long[length];
                for(int i = 0; i < length; i++) {
                    longs[i] = baseTypeArrayReference.getLongValue(i);
                }
                return longs;
            case ArrayTypeCodes.T_SHORT:
                short[] shorts = new short[length];
                for(int i = 0; i < length; i++) {
                    shorts[i] = baseTypeArrayReference.getShortValue(i);
                }
                return shorts;
            default:
                throw new IllegalStateException("Unexpected value: " + typeCode);
        }
    }

    private static Object objectArrayReference2Object(ObjectArrayReference objectArrayReference) {
        return null;
    }
}
