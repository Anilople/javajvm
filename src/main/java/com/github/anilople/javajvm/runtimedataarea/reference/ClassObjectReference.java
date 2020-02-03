package com.github.anilople.javajvm.runtimedataarea.reference;

import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.heap.JvmClassLoader;
import com.github.anilople.javajvm.runtimedataarea.Reference;
import com.github.anilople.javajvm.utils.ReferenceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * represents
 * @see java.lang.Class
 *
 * This class is per to per with java.lang.Class in real jvm
 * Every class only exists one java.lang.Class,
 * so every {@code JvmClass} must follow this rule!
 */
public class ClassObjectReference extends ObjectReference {

    private static final Logger logger = LoggerFactory.getLogger(ClassObjectReference.class);

    /**
     * memory class
     */
    private static final Map<Class<?>, ClassObjectReference> class2ClassObjectReference = new ConcurrentHashMap<>();

    private static final Map<ClassObjectReference, Class<?>> classObjectReference2Class = new ConcurrentHashMap<>();

    /**
     * Remember that new java.lang.Class() is forbidden,
     * so the initialize must be manual!
     * {@code JvmClass} per to per with {@code ClassObjectReference}
     * @see JvmClass
     * @param jvmClass {@code Class<?>} ? represents a class in java
     */
    private ClassObjectReference(JvmClass jvmClass) {
        // this object reference's type is java.lang.Class
        super(jvmClass.getLoader().loadClass(Class.class));

        final String javaLevelClassName = jvmClass.getJavaLevelClassName();

        logger.debug("make a new ClassObjectReference for JvmClass [{}] -- java level class name", javaLevelClassName);

        // hack the name
        ObjectReference stringReference = ReferenceUtils.getStringObjectReference(
                jvmClass.getLoader().loadClass(String.class),
                javaLevelClassName
        );
        this.setReference("name", stringReference);
    }

    /**
     * one JvmClass, one ClassObjectReference
     * @param jvmClass represents {@code Class<String>}, {@code Class<Object>}, int.class, void.class, etc..
     * @return self-define ClassObjectReference
     */
    public static ClassObjectReference getInstance(JvmClass jvmClass) {
        // which java.lang.Class<?> this JvmClass should be
        final Class<?> clazz = jvmClass.getRealClassInJvm();
        if(!class2ClassObjectReference.containsKey(clazz)) {
            logger.debug("{} not exists, now try to add it.", clazz);
            final JvmClass clazzJvmClass = jvmClass.getLoader().loadClass(clazz);
            ClassObjectReference classObjectReference = new ClassObjectReference(clazzJvmClass);
            class2ClassObjectReference.put(clazz, classObjectReference);
            classObjectReference2Class.put(classObjectReference, clazz);
        }
        return class2ClassObjectReference.get(clazz);
    }

    /**
     * sometimes we want to know, if give a {@code ClassObjectReference},
     * which {@code Class<?>} it represents?
     * this method will tell you.
     * @param classObjectReference
     * @return
     */
    public static Class<?> getRealClassInJvm(ClassObjectReference classObjectReference) {
        if(!classObjectReference2Class.containsKey(classObjectReference)) {
            throw new RuntimeException(classObjectReference + " not exist");
        }
        return classObjectReference2Class.get(classObjectReference);
    }

    /**
     * @see Class#getComponentType()
     * @return
     */
    public Reference getComponentType() {
        // must use this way to get java.lang.Class
        Class<?> clazz = getRealClassInJvm(this);
        Class<?> componentType = clazz.getComponentType();
        if(null == componentType) {
            return Reference.NULL;
        } else {
            JvmClassLoader jvmClassLoader = this.getJvmClass().getLoader();
            JvmClass componentTypeJvmClass = jvmClassLoader.loadClass(componentType);
            return getInstance(componentTypeJvmClass);
        }
    }

    @Override
    public String toString() {
        Class<?> clazz = classObjectReference2Class.get(this);
        return "ClassObjectReference{" + clazz + "}";
    }
}
