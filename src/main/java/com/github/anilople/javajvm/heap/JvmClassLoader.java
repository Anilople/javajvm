package com.github.anilople.javajvm.heap;

import com.github.anilople.javajvm.JavaJvmApplication;
import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.classpath.ClassContext;
import com.github.anilople.javajvm.constants.SpecialMethods;
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
        // initial the vm
        VM.initial(this);
    }

    /**
     * if a class has been loaded,
     * then simply return
     * @param className
     * @return
     */
    public JvmClass loadClass(String className) {
        // if exists already, return it
        if(classConcurrentMap.containsKey(className)) {
            return classConcurrentMap.get(className);
        }

        // load a new class
        logger.debug("load a new class not in cache: {}", className);
        if(DescriptorUtils.isArrayType(className)) {
            // array class
            this.loadArrayClass(className);
        } else {
            // non-array class
            this.loadNonArrayClass(className);
        }

        return classConcurrentMap.get(className);
    }

    /**
     * load java's class with type
     * @param clazz
     * @return
     */
    public JvmClass loadClass(Class<?> clazz) {
        // name like java.lang.Object
        String className = clazz.getName();
        // change to java/lang/Object
        String standardFormatInRuntimeClassName = className.replace('.', '/');
        return loadClass(standardFormatInRuntimeClassName);
    }

    /**
     * load a non array class
     * @param className
     * @return
     */
    private JvmClass loadNonArrayClass(String className) {
        // loading
        byte[] bytes = classContext.readClass(className);
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
        JvmClass jvmClass = new JvmClass(this, className);
        classConcurrentMap.put(className, jvmClass);
        return jvmClass;
    }
}
