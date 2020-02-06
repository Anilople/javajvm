package com.github.anilople.javajvm.heap;

import com.github.anilople.javajvm.JavaJvmApplication;
import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.classpath.Classpath;
import com.github.anilople.javajvm.utils.DescriptorUtils;
import com.github.anilople.javajvm.vm.VM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * class loader
 */
public class JvmClassLoader {

    private static final Logger logger = LoggerFactory.getLogger(JvmClassLoader.class);

    // single instance
    private static volatile JvmClassLoader INSTANCE = new JvmClassLoader(Classpath.getInstance());

    private Classpath classpath;

    /**
     * memory the class which has been loaded
     */
    private final ConcurrentMap<String, JvmClass> classConcurrentMap = new ConcurrentHashMap<>();;


    private JvmClassLoader(Classpath classpath) {
        this.classpath = classpath;
        // initial the vm
        VM.initial(this);
    }

    /**
     * @return Single case {@link JvmClassLoader}
     * @throws
     */
    synchronized public static JvmClassLoader getInstance() {
        return INSTANCE;
    }

    /**
     * if a class has been loaded,
     * then simply return
     * @return {@link JvmClass} in cache, if not exist create new one
     */
    public JvmClass loadClass(String className) {
        if(!classConcurrentMap.containsKey(className)) {
            // forbid concurrent problem
            synchronized (classConcurrentMap) {
                if(!classConcurrentMap.containsKey(className)) {
                    // load a new class
                    logger.debug("load a new class not in cache: {}", className);
                    if(DescriptorUtils.isArrayType(className)) {
                        // array class
                        this.loadArrayClass(className);
                    } else {
                        // non-array class
                        this.loadNonArrayClass(className);
                    }
                }
            }
        }

        return classConcurrentMap.get(className);
    }

    /**
     * load java's class with type
     * @param clazz
     * @return
     */
    public JvmClass loadClass(Class<?> clazz) {
        if(clazz.isPrimitive()) {
            // char, byte, boolean ...
            return loadPrimitiveClass(clazz);
        } else {
            // name like java.lang.Object
            String className = clazz.getName();
            // change to java/lang/Object
            String standardFormatInRuntimeClassName = className.replace('.', '/');
            return loadClass(standardFormatInRuntimeClassName);
        }
    }

    /**
     * load primitive class (boolean.class, char.class etc.)
     * @param clazz
     * @return
     */
    private JvmClass loadPrimitiveClass(Class<?> clazz) {
        if(!clazz.isPrimitive()) {
            throw new IllegalArgumentException(clazz + " is not primitive class");
        }
        String className = clazz.getName();

        classConcurrentMap.computeIfAbsent(
                className,
                key -> new JvmClass(this, key)
        );

        return classConcurrentMap.get(className);
    }

    /**
     * load a non array class
     * class must not exists before
     * @param className
     * @return
     * @throws IllegalArgumentException if class given initializes already.
     */
    private JvmClass loadNonArrayClass(String className) {
        if(classConcurrentMap.containsKey(className)) {
            throw new IllegalArgumentException(className + " initial already");
        }

        // loading
        byte[] bytes = classpath.readClass(className);
        // define
        ClassFile.ClassReader classReader = new ClassFile.ClassReader(bytes);
        ClassFile classFile = ClassFile.parse(classReader);

        // new a instance without initial, Why? forbid the circle in graph
        JvmClass jvmClass = new JvmClass(this);
        // add it to cache
        classConcurrentMap.put(className, jvmClass);
        // initial it
        jvmClass.initial(classFile);

        // linking
        verify(jvmClass);
        prepare(jvmClass);

        // static initializing
        if(jvmClass.existsStaticInitialMethod()) {
            JvmMethod clinit = jvmClass.getStaticInitialMethod();
            logger.debug("class {}, static init {}", jvmClass.getName(), clinit.getName());
            JavaJvmApplication.interpret(clinit);
        }

        return jvmClass;
    }

    private void verify(JvmClass jvmClass) {
        // to do
    }

    private void prepare(JvmClass jvmClass) {
        // to do
    }

    /**
     * jvms8
     * 3.9 Arrays
     * Java Virtual Machine arrays are also objects. Arrays are created and manipulated
     * using a distinct set of instructions.
     * @param className
     * @return
     */
    private JvmClass loadArrayClass(String className) {
        classConcurrentMap.computeIfAbsent(
                className,
                key -> new JvmClass(this, className)
        );
        return classConcurrentMap.get(className);
    }
}
