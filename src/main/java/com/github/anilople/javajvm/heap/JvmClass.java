package com.github.anilople.javajvm.heap;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.constants.AccessFlags;
import com.github.anilople.javajvm.constants.Descriptors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * a class in runtime,
 * generate from .class file or user define
 *
 */
public class JvmClass {

    private static final Logger logger = LoggerFactory.getLogger(JvmClass.class);

    private short accessFlags;

    private String name;

    private String superClassName;

    private String[] interfaceNames;

    private JvmConstantPool jvmConstantPool;

    /**
     * whatever static or not,
     * field's message save here
     */
    private JvmField[] jvmFields;

    /**
     * methods in this class
     */
    private JvmMethod[] jvmMethods;

    /**
     * which class loader load this class
     */
    private JvmClassLoader loader;

    private JvmClass superClass;

    private JvmClass[] interfaces;

    private JvmClass() {

    }

    public JvmClass(JvmClassLoader jvmClassLoader, ClassFile classFile) {
        this.accessFlags = classFile.getAccessFlags();
        this.name = classFile.getClassName();
        this.superClassName = classFile.existSuperClass() ? classFile.getSuperClassName() : null;
        this.interfaceNames = classFile.getInterfaceNames();
        this.jvmConstantPool = new JvmConstantPool(this, classFile.getConstantPool());
        this.jvmFields = JvmField.generateJvmFields(this, classFile.getFields());
        this.jvmMethods = JvmMethod.generateJvmMethods(this, classFile.getMethods());
        this.loader = jvmClassLoader;

        // load super class
        if(null != this.superClassName) {
            logger.debug("load super class : {}", this.superClassName);
            this.superClass = jvmClassLoader.loadClass(classFile.getSuperClassName());
        } else {
            logger.debug("class {} have no super class", this.name);
            this.superClass = null;
        }

        // interfaces from class loader and interface names
        interfaces = new JvmClass[this.interfaceNames.length];
        for(int i = 0; i < this.interfaceNames.length; i++) {
            interfaces[i] = jvmClassLoader.loadClass(this.interfaceNames[i]);
        }
    }



    /**
     * fields not only in now class,
     * but also in super classes
     *
     * @return all fields in spuer classes and now class with offset order
     */
    public static List<JvmField> getAllJvmFields(JvmClass nowClass) {
        // inherit chain
        List<JvmClass> jvmClasses = JvmClass.getInheritedClassesChain(nowClass);

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
     * how many static fields occupy size in now class ?
     * (just in now class, not in super class)
     *
     * It can optimize use cache
     *
     * @return static fields occupy size just in now class
     */
    public static int getStaticFieldsSize(JvmClass nowClass) {
        List<JvmField> allFields = JvmClass.getAllJvmFields(nowClass);

        int number = 0;

        // remember that long and double occupies 2 * 4 bytes
        for(JvmField jvmField : allFields) {
            if(!jvmField.isStatic()) {
                number += jvmField.getSize();
            }
        }

        return number;
    }

    /**
     *
     * @return now static fields occupy size in this class and its super classes
     */
    public int getNonStaticFieldsSize() {
        List<JvmClass> jvmClasses = JvmClass.getInheritedClassesChain(this);

        int number = 0;
        for(JvmClass jvmClass : jvmClasses) {
            number += JvmClass.getNonStaticFieldsSize(jvmClass);
        }

        return number;
    }

    /**
     *
     * @return static fields occupy size in this class and its super classes
     */
    public int getStaticFieldsSize() {
        List<JvmClass> jvmClasses = JvmClass.getInheritedClassesChain(this);

        int number = 0;
        for(JvmClass jvmClass : jvmClasses) {
            number += JvmClass.getStaticFieldsSize(jvmClass);
        }

        return number;
    }

    public boolean isPublic() {
        return 0 != (this.accessFlags & AccessFlags.ClassFlags.ACC_PUBLIC);
    }

    public boolean isFinal() {
        return 0 != (this.accessFlags & AccessFlags.ClassFlags.ACC_FINAL);
    }

    public boolean isSuper() {
        return 0 != (this.accessFlags & AccessFlags.ClassFlags.ACC_SUPER);
    }

    public boolean isInterface() {
        return 0 != (this.accessFlags & AccessFlags.ClassFlags.ACC_INTERFACE);
    }

    public boolean isAbstract() {
        return 0 != (this.accessFlags & AccessFlags.ClassFlags.ACC_ABSTRACT);
    }

    public boolean isSynthetic() {
        return 0 != (this.accessFlags & AccessFlags.ClassFlags.ACC_SYNTHETIC);
    }

    public boolean isAnnotation() {
        return 0 != (this.accessFlags & AccessFlags.ClassFlags.ACC_ANNOTATION);
    }

    public boolean isEnum() {
        return 0 != (this.accessFlags & AccessFlags.ClassFlags.ACC_ENUM);
    }

    public String getName() {
        return name;
    }

    public String getSuperClassName() {
        return superClassName;
    }

    public String[] getInterfaceNames() {
        return interfaceNames;
    }

    public JvmConstantPool getJvmConstantPool() {
        return jvmConstantPool;
    }

    public JvmField[] getJvmFields() {
        return jvmFields;
    }

    public JvmMethod[] getJvmMethods() {
        return jvmMethods;
    }

    public JvmClassLoader getLoader() {
        return loader;
    }

    public JvmClass getSuperClass() {
        return superClass;
    }

    public JvmClass[] getInterfaces() {
        return interfaces;
    }
}
