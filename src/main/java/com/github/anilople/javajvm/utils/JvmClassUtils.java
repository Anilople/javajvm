package com.github.anilople.javajvm.utils;

import com.github.anilople.javajvm.constants.AccessFlags;
import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.heap.JvmField;
import com.github.anilople.javajvm.heap.JvmMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * handle some functions about JvmClass JvmField JvmMethod
 */
public class JvmClassUtils {

    /**
     * there is a path from Object class to now class
     *
     * @return the path in order (from ancestor to now)
     */
    public static List<JvmClass> getInheritedClassesChain(JvmClass jvmClass) {
        List<JvmClass> chain = new ArrayList<>(4);

        // add class, from child to ancestor
        for(JvmClass nowClass = jvmClass; null != nowClass; nowClass = nowClass.getSuperClass()) {
            chain.add(nowClass);
        }

        // reverse, make order with ancestor to child
        Collections.reverse(chain);

        return chain;
    }

    /**
     * how many non static fields occupy size in now class ?
     * (just in now class, not in super class)
     *
     * It can optimize use cache
     *
     * @return non static fields occupy size just in now class
     */
    public static int getNonStaticFieldsSize(JvmClass nowClass) {
        int number = 0;

        // remember that long and double occupies 2 * 4 bytes
        for(JvmField jvmField : nowClass.getJvmFields()) {
            if(!jvmField.isStatic()) {
                number += jvmField.getSize();
            }
        }

        return number;
    }

    /**
     * @param jvmClass a class
     * @param accessFlag method's access flag
     * @return methods with access flag
     */
    public static List<JvmMethod> getMethodsWith(JvmClass jvmClass, short accessFlag) {
        List<JvmMethod> methods = new ArrayList<>();
        for(JvmMethod jvmMethod : jvmClass.getJvmMethods()) {
            if(0 != (jvmMethod.getAccessFlags() & accessFlag)) {
                // match this access flag
                methods.add(jvmMethod);
            }
        }
        return methods;
    }

    /**
     * @param jvmClass
     * @return static methods
     */
    public static List<JvmMethod> getStaticMethods(JvmClass jvmClass) {
        return getMethodsWith(jvmClass, AccessFlags.MethodFlags.ACC_STATIC);
    }

    /**
     *
     * @param jvmClass
     * @return non static methods
     */
    public static List<JvmMethod> getNonStaticMethods(JvmClass jvmClass) {
        List<JvmMethod> nonStaticMethods = new ArrayList<>();
        for(JvmMethod jvmMethod : jvmClass.getJvmMethods()) {
            if(!jvmMethod.isStatic()) {
                // non static method
                nonStaticMethods.add(jvmMethod);
            }
        }
        return nonStaticMethods;
    }

    /**
     * judge method exist in a class(include super classes) or not
     * @param jvmClass
     * @param name
     * @param descriptor
     * @return
     */
    public static boolean existMethod(JvmClass jvmClass, String name, String descriptor) {
        for(JvmClass nowClass = jvmClass; null != nowClass; nowClass = nowClass.getSuperClass()) {
            for(JvmMethod jvmMethod : nowClass.getJvmMethods()) {
                if(jvmMethod.getName().equals(name) && jvmMethod.getDescriptor().equals(descriptor)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * get a method from class(include super classes) whether it is static or not
     * @param jvmClass
     * @param name
     * @param descriptor
     * @return null if method does not existed
     */
    public static JvmMethod getMethod(JvmClass jvmClass, String name, String descriptor) {
        for(JvmClass nowClass = jvmClass; null != nowClass; nowClass = nowClass.getSuperClass()) {
            for(JvmMethod jvmMethod : nowClass.getJvmMethods()) {
                if(jvmMethod.getName().equals(name) && jvmMethod.getDescriptor().equals(descriptor)) {
                    return jvmMethod;
                }
            }
        }
        return null;
    }

    /**
     * how many static fields occupy size in now class ?
     * (just in now class, not in super class)
     *
     * It can optimize use cache
     *
     * @return static fields occupy size just in now class
     */
    public static int getStaticFieldsSize(JvmClass nowClass) {
        List<JvmField> allFields = JvmClassUtils.getAllJvmFields(nowClass);

        int number = 0;

        // remember that long and double occupies 2 * 4 bytes
        for(JvmField jvmField : allFields) {
            if(jvmField.isStatic()) {
                number += jvmField.getSize();
            }
        }

        return number;
    }

    /**
     * fields not only in now class,
     * but also in super classes
     *
     * @return all fields in spuer classes and now class with offset order
     */
    public static List<JvmField> getAllJvmFields(JvmClass nowClass) {
        // inherit chain
        List<JvmClass> jvmClasses = JvmClassUtils.getInheritedClassesChain(nowClass);

        // all fields save here
        List<JvmField> allFields = new ArrayList<>();

        // Traversing from ancestor to now class
        for(JvmClass jvmClass : jvmClasses) {
            // traversing fields in this class
            for(JvmField jvmField : jvmClass.getJvmFields()) {
                allFields.add(jvmField);
            }
        }

        return allFields;
    }

    /**
     * get some fields in now class which
     * satisfies that 0 != (field's accessFlags & accessFlag),
     * like making a filter (x -> 0 != (x & accessFlag)), x is field's access flag
     * @param nowClass
     * @param accessFlag
     * @return
     */
    public static List<JvmField> getFieldsWithAccessFlag(JvmClass nowClass, short accessFlag) {
        List<JvmField> jvmFields = new ArrayList<>();
        for(JvmField jvmField : nowClass.getJvmFields()) {
            if(0 != (jvmField.getAccessFlags() & accessFlag)) {
                jvmFields.add(jvmField);
            }
        }
        return jvmFields;
    }

    /**
     *
     * @param nowClass
     * @return static fields in now class by order
     */
    public static List<JvmField> getStaticFields(JvmClass nowClass) {
        // get static fields
        return JvmClassUtils.getFieldsWithAccessFlag(nowClass, AccessFlags.ACC_STATIC);
    }

    /**
     * now class exists this field or not.
     * Whatever field is static or not, it doesn't matter.
     * This method just make a simple check
     * @param nowClass
     * @param jvmField
     * @return
     */
    public static boolean exists(JvmClass nowClass, JvmField jvmField) {
        for(JvmField jvmFieldInNowClass : nowClass.getJvmFields()) {
            if(jvmFieldInNowClass.equals(jvmField)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Whatever now class exist this field,
     * or its ancestor exist this field,
     * a true will return;
     * else return a false.
     * @param nowClass
     * @param jvmField
     * @return
     */
    public static boolean existsWithAncestor(JvmClass nowClass, JvmField jvmField) {
        if(nowClass.exists(jvmField)) {
            return true;
        } else {
            // find in super class
            return nowClass.existsSuperClass() && existsWithAncestor(nowClass.getSuperClass(), jvmField);
        }
    }

    /**
     * find that which jvm class this static field belong to.
     * Because some times there is inheritance with static field between classes
     * @param nowClass
     * @param jvmField must be a static field
     * @return
     */
    public static JvmClass getJvmClassStaticFieldBelongTo(JvmClass nowClass, JvmField jvmField) {
        if(!jvmField.isStatic()) {
            throw new RuntimeException("Please pass a static field. " + jvmField + " is not static");
        }

        if(nowClass.exists(jvmField)) {
            return nowClass;
        } else {
            // find in super class(if field also not exists in ancestor, an Exception will occur)
            return getJvmClassStaticFieldBelongTo(nowClass.getSuperClass(), jvmField);
        }
    }
}
