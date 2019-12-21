package com.github.anilople.javajvm.heap;

import com.github.anilople.javajvm.classfile.ClassFile;
import com.github.anilople.javajvm.constants.AccessFlags;
import com.github.anilople.javajvm.constants.SpecialMethods;
import com.github.anilople.javajvm.runtimedataarea.LocalVariables;
import com.github.anilople.javajvm.utils.JvmClassUtils;
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

    /**
     * static fields cache
     * to save current class's static fields's value
     */
    private LocalVariables staticFieldsValue;


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

        // static fields value
        this.staticFieldsValue = new LocalVariables(JvmClassUtils.getStaticFieldsSize(this));
    }


    /**
     * judge method exist in this class or not
     * @param name
     * @param descriptor
     * @return
     */
    public boolean existMethod(String name, String descriptor) {
        return JvmClassUtils.existMethod(this, name, descriptor);
    }

    /**
     * get method in this class
     * @param name
     * @param descriptor
     * @return
     */
    public JvmMethod getMethod(String name, String descriptor) {
        return JvmClassUtils.getMethod(this, name, descriptor);
    }

    /**
     *
     * @return now static fields occupy size in this class and its super classes
     */
    public int getNonStaticFieldsSize() {
        List<JvmClass> jvmClasses = JvmClassUtils.getInheritedClassesChain(this);

        int number = 0;
        for(JvmClass jvmClass : jvmClasses) {
            number += JvmClassUtils.getNonStaticFieldsSize(jvmClass);
        }

        return number;
    }

    /**
     *
     * @return static fields occupy size in this class and its super classes
     */
    public int getStaticFieldsSize() {
        List<JvmClass> jvmClasses = JvmClassUtils.getInheritedClassesChain(this);

        int number = 0;
        for(JvmClass jvmClass : jvmClasses) {
            number += JvmClassUtils.getStaticFieldsSize(jvmClass);
        }

        return number;
    }

    /**
     * if this class exists field, return true;
     * else return false.
     * @param jvmField
     * @return
     */
    public boolean exists(JvmField jvmField) {
        return JvmClassUtils.exists(this, jvmField);
    }

    /**
     * @return true if this class exist super class
     */
    public boolean existsSuperClass() {
        return null != this.superClass;
    }

    /**
     * check this class exist "<clinit>" method or not
     * @return
     */
    public boolean existsStaticInitialMethod() {
        for(JvmMethod jvmMethod : this.getJvmMethods()) {
            if(SpecialMethods.CLINIT.equals(jvmMethod.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @throws RuntimeException if there is no "<clinit>" method
     * @return "<clinit>" method
     */
    public JvmMethod getStaticInitialMethod() {
        for(JvmMethod jvmMethod : this.getJvmMethods()) {
            if(SpecialMethods.CLINIT.equals(jvmMethod.getName())) {
                return jvmMethod;
            }
        }
        throw new RuntimeException(SpecialMethods.CLINIT + " doesn't exists in class " + this.getName());
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

    public LocalVariables getStaticFieldsValue() {
        return staticFieldsValue;
    }
}
