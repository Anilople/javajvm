package com.github.anilople.javajvm.utils;

import com.github.anilople.javajvm.cachepool.StringPool;
import com.github.anilople.javajvm.constants.ArrayTypeCodes;
import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.heap.JvmClassLoader;
import com.github.anilople.javajvm.runtimedataarea.LocalVariables;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.runtimedataarea.reference.*;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A bridge between real java object and self-define reference
 */
public class ReferenceUtils {

    private static final Logger logger = LoggerFactory.getLogger(ReferenceUtils.class);

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
        return object2Reference(new HashMap<>(), jvmClassLoader, object);
    }

    /**
     * @param cache save the references to forbid stack over flow by graph circle
     * @param jvmClassLoader
     * @param object
     * @return
     * @throws IllegalAccessException
     */
    private static Reference object2Reference(
            Map<Object, Reference> cache, JvmClassLoader jvmClassLoader, Object object
    ) throws IllegalAccessException {
        if(null == object) {
            return Reference.NULL;
        }

        if(cache.containsKey(object)) {
            return cache.get(object);
        }

        logger.trace("{} not in cache.", object);

        final Class<?> clazz = object.getClass();
        // array
        if(clazz.isArray()) {
            return array2ArrayReference(cache, jvmClassLoader, object);
        }

        // object
        return object2ObjectReference(jvmClassLoader, object);
    }

    /**
     * convert object to self-define object reference
     * @param jvmClassLoader
     * @param object
     * @return
     * @throws IllegalAccessException
     */
    static ObjectReference object2ObjectReference(
            JvmClassLoader jvmClassLoader, Object object
    ) throws IllegalAccessException {
        return object2ObjectReference(new HashMap<>(), jvmClassLoader, object);
    }

    private static ObjectReference object2ObjectReference(
            Map<Object, Reference> cache,
            JvmClassLoader jvmClassLoader, Object object
    ) throws IllegalAccessException {
        final Class<?> clazz = object.getClass();
        // get the JvmClass of object
        JvmClass jvmClass = jvmClassLoader.loadClass(clazz);
        ObjectReference objectReference = new ObjectReference(jvmClass);
        // cache it
        cache.put(object, objectReference);

        // converter the static fields, todo

        // converter the non-static fields
        List<Field> nonStaticFields = ReflectionUtils.getNonStaticFieldsFromAncestor(clazz);
        int nonStaticFieldCurrentOffset = 0;
        for(Field nonStaticField : nonStaticFields) {
            if(nonStaticField.getType().isPrimitive()) {
                setPrimitive2ObjectField(objectReference, nonStaticFieldCurrentOffset, object, nonStaticField);
            } else {
                setObjectField2ObjectReference(
                        cache,
                        objectReference, nonStaticFieldCurrentOffset,
                        jvmClassLoader,
                        object, nonStaticField
                );
            }
            nonStaticFieldCurrentOffset += ReflectionUtils.getFieldSize(nonStaticField);
        }

        return objectReference;
    }

    /**
     * convert an array object to self-define array reference
     * @param cache
     * @param jvmClassLoader
     * @param arrayObject array
     * @return array reference
     * @throws IllegalAccessException
     */
    static ArrayReference array2ArrayReference(
            Map<Object, Reference> cache, JvmClassLoader jvmClassLoader, Object arrayObject
    ) throws IllegalAccessException {
        int dimensions = DescriptorUtils.getDimensions(arrayObject.getClass().getName());
        if(dimensions <= 0) {
            throw new RuntimeException(arrayObject + " is not array");
        }
        if(1 == dimensions) {
            // end the recursion
            final Class<?> componentType = arrayObject.getClass().getComponentType();
            if(componentType.isPrimitive()) {
                // primitive should be cached too
                BaseTypeArrayReference baseTypeArrayReference = singleDimensionPrimitiveArray2ArrayReference(arrayObject);
                cache.put(arrayObject, baseTypeArrayReference);
            } else {
                ObjectArrayReference objectArrayReference = singleDimensionObjectArray2ObjectArrayReference(cache, jvmClassLoader, arrayObject);
                cache.put(arrayObject, objectArrayReference);
            }

            return (ArrayReference) cache.get(arrayObject);
        }


        // multiple dimensions
        throw new RuntimeException("Now cannot support multiple dimensions array");
    }

    /**
     * @param primitiveArrayObject 1 dimension array of primitive type
     * @return self-define array reference
     */
    static BaseTypeArrayReference singleDimensionPrimitiveArray2ArrayReference(Object primitiveArrayObject) {
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
     *
     * @param cache
     * @param jvmClassLoader
     * @param objectArrayObject 1 dimension array of object reference
     * @return self-define array reference
     * @throws IllegalAccessException
     */
    static ObjectArrayReference singleDimensionObjectArray2ObjectArrayReference(
            Map<Object, Reference> cache,
            JvmClassLoader jvmClassLoader, Object objectArrayObject
    ) throws IllegalAccessException {
        Object[] objects = (Object[]) objectArrayObject;
        Class<?> componentClass = objects.getClass().getComponentType();
        ObjectArrayReference objectArrayReference = new ObjectArrayReference(
                jvmClassLoader.loadClass(componentClass),
                objects.length
        );
        // cache it
        cache.put(objectArrayObject, objectArrayReference);

        // set the values to array
        for(int i = 0; i < objects.length; i++) {
            Reference reference = object2Reference(cache, jvmClassLoader, objects[i]);
            objectArrayReference.setReference(i, reference);
        }
        return objectArrayReference;
    }

    /**
     * set the field's value to position offset
     * long and double will occupy 2 locations
     * @param localVariables super class of ObjectReference
     * @param offset position in the object
     * @param field must be primitive type
     */
    private static void setPrimitive2ObjectField(LocalVariables localVariables, int offset, Object object, Field field) throws IllegalAccessException {
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
     * the value must be object reference
     * @param cache
     * @param localVariables
     * @param fieldCurrentOffset
     * @param jvmClassLoader
     * @param object
     * @param field
     */
    private static void setObjectField2ObjectReference(
            Map<Object, Reference> cache,
            LocalVariables localVariables, int fieldCurrentOffset,
            JvmClassLoader jvmClassLoader,
            Object object, Field field
    ) throws IllegalAccessException {
        // get the field
        field.setAccessible(true);
        Object fieldObject = field.get(object);
        // resolve the reference
        Reference reference = object2Reference(cache, jvmClassLoader, fieldObject);
        // set the reference
        localVariables.setReference(fieldCurrentOffset, reference);
    }

    /**
     * fetch the reference from self-define object reference
     * and convert the reference fetched to a real object value in JVM,
     * then set this real object value to object's field
     * @param cache
     * @param object
     * @param field
     * @param jvmClassLoader
     * @param objectReference
     * @param offset
     */
    private static void setReference2ObjectField(
            Map<Reference, Object> cache,
            Object object, Field field,
            JvmClassLoader jvmClassLoader,
            ObjectReference objectReference, int offset
    ) throws IllegalAccessException {
       Reference reference = objectReference.getReference(offset);
       Object value = reference2Object(cache, reference);
       // set the value
       field.setAccessible(true);
       field.set(object, value);
    }


    /**
     * convert self-define reference to a real object
     * @param reference
     * @return
     */
    public static Object reference2Object(Reference reference) throws IllegalAccessException {
        return reference2Object(new HashMap<>(), reference);
    }

    /**
     *
     * @param cache cache object, forbid stack over flow (recursion cause by graph's circle)
     * @param reference
     * @return
     * @throws IllegalAccessException
     */
    private static Object reference2Object(
            Map<Reference, Object> cache, Reference reference
    ) throws IllegalAccessException {
        // null
        if(Reference.isNull(reference)) {
            return null;
        }
        // look up from cache, forbid graph circle!!!
        if(cache.containsKey(reference)) {
            return cache.get(reference);
        }
        if(reference instanceof ObjectReference) {
            return objectReference2Object(cache, (ObjectReference) reference);
        }
        if(reference instanceof ArrayReference) {
            return arrayReference2Object(cache, (ArrayReference) reference);
        }
        throw new IllegalStateException("Unexpected value: " + reference.toString());
    }

    /**
     * convert self-define object reference to real object,
     * but notice that java.lang.Class cannot
     * @param objectReference
     * @return
     * @throws IllegalAccessException
     */
    static Object objectReference2Object(ObjectReference objectReference) throws IllegalAccessException {
        return objectReference2Object(new HashMap<>(), objectReference);
    }

    /**
     * convert a self define object reference to a real object
     * @param cache cache object, forbid stack over flow (recursion cause by graph's circle)
     * @param objectReference
     * @return
     * @throws IllegalAccessException
     */
    private static Object objectReference2Object(
            Map<Reference, Object> cache, ObjectReference objectReference
    ) throws IllegalAccessException {
        final JvmClass jvmClass = objectReference.getJvmClass();
        final Class<?> clazz = jvmClass.getRealClassInJvm();
        if(objectReference instanceof ClassObjectReference) {
            return ClassObjectReference.getRealClassInJvm((ClassObjectReference) objectReference);
        }

        // check cache
        if(cache.containsKey(objectReference)) {
            // in cache already, so just get it
            return cache.get(objectReference);
        }

        // not in cache, new one
        Objenesis objenesis = new ObjenesisStd();
        ObjectInstantiator<?> objectInstantiator = objenesis.getInstantiatorOf(clazz);
//        object = clazz.newInstance();
        // new instance by objenesis
        Object object = objectInstantiator.newInstance();

        // then add it to cache
        cache.put(objectReference, object);

        // change fields in object
        setObjectFields(cache, object, objectReference);

        return object;
    }

    /**
     * set the values in self define object reference to real object's fields
     * @param cache
     * @param object
     * @param objectReference
     * @throws IllegalAccessException
     */
    private static void setObjectFields(
            Map<Reference, Object> cache, Object object, ObjectReference objectReference
    ) throws IllegalAccessException {
        if(object instanceof Throwable) {
            // for the bug in jdk 8
            setThrowableFields(cache, object, objectReference);
            return;
        }

        final JvmClass jvmClass = objectReference.getJvmClass();
        final Class<?> clazz = jvmClass.getRealClassInJvm();
        final JvmClassLoader jvmClassLoader = jvmClass.getLoader();

        List<Field> fields = ReflectionUtils.getNonStaticFieldsFromAncestor(clazz);

        int realOffset = 0;
        // change object's fields value
        for(int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            if(field.getType().isPrimitive()) {
                setPrimitive2ObjectField(object, field, objectReference, realOffset);
            } else {
                setReference2ObjectField(cache, object, field, jvmClassLoader, objectReference, realOffset);
            }
            realOffset += ReflectionUtils.getFieldSize(field);
        }
    }

    /**
     * {@code Throwable.class.getDeclaredField("backtrace")} will get Exception.
     * So use this method to handle it manual
     * @see <a href="https://bugs.java.com/bugdatabase/view_bug.do?bug_id=8033735">JDK-8033735 : make Throwable.backtrace visible to Class.getDeclaredField again</a>
     * @param cache
     * @param object
     * @param objectReference
     * @throws IllegalAccessException
     */
    private static void setThrowableFields(
            Map<Reference, Object> cache, Object object, ObjectReference objectReference
    ) throws IllegalAccessException {
        if(! (object instanceof Throwable)) {
            throw new RuntimeException(object.getClass() + " is not subclass of " + Throwable.class);
        }

        final JvmClass jvmClass = objectReference.getJvmClass();
        final Class<?> clazz = jvmClass.getRealClassInJvm();
        final JvmClassLoader jvmClassLoader = jvmClass.getLoader();

        List<Field> fields = ReflectionUtils.getNonStaticFieldsFromAncestor(clazz);
        // private transient Object backtrace;
        // cannot get by reflection in jdk 8
        // we handle it manually
        Reference backtrace = objectReference.getReference(0);
        if(Reference.isNull(backtrace)) {
            // the default value in object is null, so we do nothing
        } else {
            // cannot set field "backtrace"'s value by reflection
            // so throw an exception here
            throw new IllegalAccessException("Cannot change backtrace in " + Throwable.class + ", " + backtrace);
        }

        // skip private transient Object backtrace;
        // from offset 1 not 0
        int realOffset = 1;
        // change object's fields value
        for(int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            if(field.getType().isPrimitive()) {
                setPrimitive2ObjectField(object, field, objectReference, realOffset);
            } else {
                setReference2ObjectField(cache, object, field, jvmClassLoader, objectReference, realOffset);
            }
            realOffset += ReflectionUtils.getFieldSize(field);
        }
    }

    /**
     * set the primitive value
     * from self-define reference to object's field
     * @param object
     * @param field
     * @param objectReference
     * @param offset
     * @throws IllegalAccessException
     */
    private static void setPrimitive2ObjectField(
            Object object, Field field,
            ObjectReference objectReference, int offset
    ) throws IllegalAccessException {
        if(!field.getType().isPrimitive()) {
            throw new RuntimeException(field + " is not primitive");
        }
        field.setAccessible(true);
        final Class<?> fieldType = field.getType();
        final Class<?> type = fieldType;
        if(type.equals(boolean.class)) {
            boolean value = objectReference.getBooleanValue(offset);
            field.setBoolean(object, value);
        } else if(type.equals(byte.class)) {
            byte value = objectReference.getByteValue(offset);
            field.setByte(object, value);
        } else if(type.equals(short.class)) {
            short value = objectReference.getShortValue(offset);
            field.setShort(object, value);
        } else if(type.equals(char.class)) {
            char value = objectReference.getCharValue(offset);
            field.setChar(object, value);
        } else if(type.equals(int.class)) {
            int value = objectReference.getIntValue(offset);
            field.setInt(object, value);
        } else if(type.equals(float.class)) {
            float value = objectReference.getFloatValue(offset);
            field.setFloat(object, value);
        } else if(type.equals(long.class)) {
            long value = objectReference.getLongValue(offset);
            field.setLong(object, value);
        } else if(type.equals(double.class)) {
            double value = objectReference.getDoubleValue(offset);
            field.setDouble(object, value);
        } else {
            throw new IllegalArgumentException("Cannot set type " + type);
        }
    }


    private static Object arrayReference2Object(
            Map<Reference, Object> cache, ArrayReference arrayReference
    ) throws IllegalAccessException {
        if(arrayReference instanceof BaseTypeArrayReference) {
            // primitive type array does not need cache
            return baseTypeArrayReference2Object((BaseTypeArrayReference) arrayReference);
        } else if(arrayReference instanceof ObjectArrayReference) {
            return objectArrayReference2Object(cache, (ObjectArrayReference) arrayReference);
        } else {
            throw new IllegalStateException("Unexpected value: " + arrayReference.toString());
        }
    }

    /**
     * convert self-define BaseTypeArrayReference to real primitive type array
     * @param baseTypeArrayReference
     * @return
     */
    static Object baseTypeArrayReference2Object(BaseTypeArrayReference baseTypeArrayReference) {
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

    /**
     * convert self-define ObjectArrayReference to real reference type array
     * @param cache
     * @param objectArrayReference
     * @return
     * @throws IllegalAccessException
     */
    private static Object objectArrayReference2Object(
            Map<Reference, Object> cache, ObjectArrayReference objectArrayReference
    ) throws IllegalAccessException {
        final int length = objectArrayReference.length();
        Object[] objects = null;
        // check cache
        if(cache.containsKey(objectArrayReference)) {
            objects = (Object[]) cache.get(objectArrayReference);
        } else {
            // not in cache, new one
            final Class<?> componentType =  objectArrayReference.getComponentType().getRealClassInJvm();
            objects = (Object[]) Array.newInstance(componentType, length);
            // then add it to cache
            cache.put(objectArrayReference, objects);
        }
        for(int i = 0; i < length; i++) {
            Object object = reference2Object(objectArrayReference.getReference(i));
            objects[i] = object;
        }
        return objects;
    }

    /**
     * get the local variable from local variable
     * @param localVariables
     * @param offset
     * @param type
     * @return
     * @throws IllegalAccessException
     */
    static Object getLocalVariableByClassType(
            LocalVariables localVariables, int offset, Class<?> type
    ) {
        if(type.isPrimitive()) {
            // primitive value
            return getPrimitiveLocalVariableByClassType(localVariables, offset, type);
        } else {
            // reference value
            Reference reference = localVariables.getReference(offset);
            try {
                return reference2Object(reference);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * get the primitive value from local variables
     * @param localVariables
     * @param offset
     * @param type
     * @return
     */
    static Object getPrimitiveLocalVariableByClassType(
            LocalVariables localVariables, int offset, Class<?> type
    ) {
        if(type.equals(boolean.class)) {
            return localVariables.getBooleanValue(offset);
        } else if(type.equals(byte.class)) {
            return localVariables.getByteValue(offset);
        } else if(type.equals(short.class)) {
            return localVariables.getShortValue(offset);
        } else if(type.equals(char.class)) {
            return localVariables.getCharValue(offset);
        } else if(type.equals(int.class)) {
            return localVariables.getIntValue(offset);
        } else if(type.equals(float.class)) {
            return localVariables.getFloatValue(offset);
        } else if(type.equals(long.class)) {
            return localVariables.getLongValue(offset);
        } else if(type.equals(double.class)) {
            return localVariables.getDoubleValue(offset);
        } else {
            throw new IllegalArgumentException("Cannot set type " + type);
        }
    }

    /**
     * emulator "arraycopy" in System
     * @see java.lang.System arraycopy
     * @param src
     * @param srcPos
     * @param dest
     * @param destPos
     * @param length
     */
    public static void arrayCopy(
            ArrayReference src, int srcPos,
            ArrayReference dest, int destPos,
            int length
    ) {
        if(!src.getClass().equals(dest.getClass())) {
            throw new IllegalArgumentException(src + " type not same as " + dest);
        }
        if(src instanceof BaseTypeArrayReference) {
            arrayCopy((BaseTypeArrayReference) src, srcPos, (BaseTypeArrayReference) dest, destPos, length);
        } else if(src instanceof ObjectArrayReference) {
            throw new RuntimeException("Now cannot support array copy " + ObjectArrayReference.class);
        } else {
            throw new IllegalStateException("Wrong type " + src);
        }
    }

    /**
     * copy the primitive values of self-define bast type array
     * @param src
     * @param srcPos
     * @param dest
     * @param destPos
     * @param length
     */
    private static void arrayCopy(
            BaseTypeArrayReference src, int srcPos,
            BaseTypeArrayReference dest, int destPos,
            int length
    ) {
        if(src.getTypeCode() != dest.getTypeCode()) {
            throw new IllegalArgumentException("type code not same");
        }
        for(int i = 0; i < length; i++) {
            arrayCopyPrimitiveOne(src, srcPos + i, dest, destPos + i);
        }
    }

    /**
     * just copy 1 primitive element in array
     * @param src
     * @param srcIndex
     * @param dest
     * @param destIndex
     */
    private static void arrayCopyPrimitiveOne(
            BaseTypeArrayReference src, int srcIndex,
            BaseTypeArrayReference dest, int destIndex
    ) {
        if(src.getTypeCode() != dest.getTypeCode()) {
            throw new IllegalArgumentException("type code not same");
        }
        final byte typeCode = src.getTypeCode();
        switch (typeCode) {
            case ArrayTypeCodes.T_BOOLEAN:
                dest.setBooleanValue(destIndex, src.getBooleanValue(srcIndex));
                break;
            case ArrayTypeCodes.T_BYTE:
                dest.setByteValue(destIndex, src.getByteValue(srcIndex));
                break;
            case ArrayTypeCodes.T_CHAR:
                dest.setCharValue(destIndex, src.getCharValue(srcIndex));
                break;
            case ArrayTypeCodes.T_DOUBLE:
                dest.setDoubleValue(destIndex, src.getDoubleValue(srcIndex));
                break;
            case ArrayTypeCodes.T_FLOAT:
                dest.setFloatValue(destIndex, src.getFloatValue(srcIndex));
                break;
            case ArrayTypeCodes.T_INT:
                dest.setIntValue(destIndex, src.getIntValue(srcIndex));
                break;
            case ArrayTypeCodes.T_LONG:
                dest.setLongValue(destIndex, src.getLongValue(srcIndex));
                break;
            case ArrayTypeCodes.T_SHORT:
                dest.setShortValue(destIndex, src.getShortValue(srcIndex));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + typeCode);
        }
    }
}
