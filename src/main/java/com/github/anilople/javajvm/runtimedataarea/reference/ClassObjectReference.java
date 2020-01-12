package com.github.anilople.javajvm.runtimedataarea.reference;

import com.github.anilople.javajvm.heap.JvmClass;
import com.github.anilople.javajvm.utils.ReferenceUtils;
import com.github.anilople.javajvm.utils.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * represents
 * @see java.lang.Class
 *
 * This class is per to per with java.lang.Class in real jvm
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
     * @param jvmClass must represents java.lang.Class
     * @param clazz
     */
    private ClassObjectReference(JvmClass jvmClass, Class<?> clazz) {
        super(jvmClass);
        if (!jvmClass.isSameName(Class.class)) {
            throw new RuntimeException(jvmClass.getName() + "'s type is not java.lang.Class");
        }
        // hack the name
        ObjectReference stringReference = ReferenceUtils.getStringObjectReference(
                jvmClass.getLoader().loadClass(String.class),
                clazz.getName()
        );
        int nameOffset = ReflectionUtils.getNonStaticOffset(Class.class, "name");
        this.setReference(nameOffset, stringReference);
    }

    /**
     *
     * @param jvmClass represents {@code Class<String>}, {@code Class<Object>}, int.class, void.class, etc..
     * @return self-define ClassObjectReference
     */
    public static ClassObjectReference getInstance(JvmClass jvmClass) {
        // which java.lang.Class<?> this JvmClass should be
        final Class<?> clazz = jvmClass.getRealClassInJvm();
        // do not consider concurrent problem
        if(!class2ClassObjectReference.containsKey(clazz)) {
            logger.debug("{} not exists, now try to add it.", clazz);
            // it is special, because we want the object reference to java.lang.Class
            final JvmClass classJvmClass = jvmClass.getLoader().loadClass(Class.class);
            ClassObjectReference classObjectReference = new ClassObjectReference(classJvmClass, clazz);
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

    @Override
    public String toString() {
        Class<?> clazz = classObjectReference2Class.get(this);
        return "ClassObjectReference{" + clazz + "}";
    }
}
