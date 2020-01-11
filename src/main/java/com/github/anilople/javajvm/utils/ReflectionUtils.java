package com.github.anilople.javajvm.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * reflection utils
 * handle runtime
 * Must not interact with self-define reference !!! 
 */
public class ReflectionUtils {

    /**
     * the type's size in local variables and operand stack
     * long and double will occupy 2 size
     * other is 1
     * @param clazz
     * @return
     */
    public static int getClassSize(Class<?> clazz) {
        if(clazz.equals(long.class) || clazz.equals(double.class)) {
            return 2;
        } else {
            return 1;
        }
    }

    /**
     * the field's size in local variables and operand stack
     * long and double will occupy 2 size
     * other is 1
     * @param field
     * @return
     */
    public static int getFieldSize(Field field) {
        Class<?> fieldType = field.getType();
        return getClassSize(fieldType);
    }


    /**
     * get all field from ancestor to current class
     * keep the order
     * @param clazz current class
     * @return
     */
    public static List<Field> getAllFieldsFromAncestor(Class<?> clazz) {
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
    public static List<Field> getNonStaticFieldsFromAncestor(Class<?> clazz) {
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
    public static List<Field> getStaticFieldsFromAncestor(Class<?> clazz) {
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
     * get the primitive class
     * boolean.class
     * byte.class
     * short.class
     * char.class
     * int.class
     * long.class
     * float.class
     * double.class
     * @see java.lang.Class
     * @param name
     * @return
     */
    public static Class<?> getPrimitiveClass(String name) {
        Method method = null;
        try {
            method = Class.class.getDeclaredMethod("getPrimitiveClass", String.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        method.setAccessible(true);
        Class<?> clazz = null;
        try {
            clazz = (Class<?>) method.invoke(null, name);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return clazz;
    }
}
