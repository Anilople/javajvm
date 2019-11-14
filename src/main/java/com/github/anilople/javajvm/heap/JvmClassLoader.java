package com.github.anilople.javajvm.heap;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.classpath.ClassContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * class loader
 */
public class JvmClassLoader {

    private static final Logger logger = LoggerFactory.getLogger(JvmClassLoader.class);

    private ClassContext classContext;

    /**
     * memory the class which has been loaded
     */
    private ConcurrentMap<String, JvmClass> classConcurrentMap;

    private JvmClassLoader() {

    }

    public JvmClassLoader(ClassContext classContext) {
        this.classContext = classContext;
        this.classConcurrentMap = new ConcurrentHashMap<>();
    }

    /**
     * if a class has been loaded,
     * then simply return
     * @param className
     * @return
     */
    public JvmClass loadClass(String className) {
        if(!classConcurrentMap.containsKey(className)) {
            this.loadNonArrayClass(className);
        }
        return classConcurrentMap.get(className);
    }

    /**
     * load a non array class
     * @param className
     * @return
     */
    public JvmClass loadNonArrayClass(String className) {
        // loading
        byte[] bytes = classContext.readClass(className);
        // define
        ClassFile.ClassReader classReader = new ClassFile.ClassReader(bytes);
        ClassFile classFile = ClassFile.parse(classReader);
        JvmClass jvmClass = new JvmClass(this, classFile);
        // load super classes have been done in new JvmClass, may cause dead loop
        classConcurrentMap.put(className, jvmClass);

        // linking
        verify(jvmClass);
        prepare(jvmClass);

        // initializing
        return jvmClass;
    }

    public void verify(JvmClass jvmClass) {
        // to do
    }

    public void prepare(JvmClass jvmClass) {
        // to do
    }
}
